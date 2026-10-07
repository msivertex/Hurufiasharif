const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// 1. Unauthenticated checks
test("Unauthenticated: cannot read or write user profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: "alice@test.com",
    coins: 50,
    level: 1,
    streak: 0,
    selectedLanguage: "BN",
    selectedQariVoice: "MALE_QARI"
  }));
});

// 2. Owner access check
test("Owner: can create and read own user profile with valid schema", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: "alice@test.com",
    displayName: "Alice",
    coins: 100,
    level: 2,
    streak: 3,
    selectedLanguage: "BN",
    selectedQariVoice: "MALE_QARI"
  }));

  const doc = await aliceDb.collection("users").doc(ALICE_UID).get();
  assertSucceeds(Promise.resolve(doc));
});

// 3. Cross-user isolation check
test("Cross-user: Bob cannot read or write Alice's profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await aliceDb.collection("users").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: "alice@test.com",
    coins: 100,
    level: 2,
    streak: 3,
    selectedLanguage: "BN",
    selectedQariVoice: "MALE_QARI"
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).update({
    coins: 9999
  }));
});

// 4. Subcollection chapter progress
test("Chapter progress: owner can write progress, cross-user cannot", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("progress").doc("chapter_1").set({
    userId: ALICE_UID,
    chapterId: "chapter_1",
    unlocked: true,
    completed: true,
    completedPages: 5,
    totalPages: 5,
    stars: 3
  }));

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("progress").doc("chapter_1").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("progress").doc("chapter_1").set({
    userId: BOB_UID,
    chapterId: "chapter_1",
    unlocked: true,
    completed: true,
    completedPages: 5,
    totalPages: 5
  }));
});

// 5. Schema validation rejection
test("Schema rejection: invalid language rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(ALICE_UID).set({
    userId: ALICE_UID,
    email: "alice@test.com",
    coins: 100,
    level: 1,
    streak: 0,
    selectedLanguage: "INVALID_LANG",
    selectedQariVoice: "MALE_QARI"
  }));
});
