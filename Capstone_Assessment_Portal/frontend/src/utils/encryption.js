// Function to encrypt password

import JSEncrypt from "jsencrypt";


export const encryptPassword = (password, publicKey) => {
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(publicKey);

  const encryptedPassword = encrypt.encrypt(password);

  if (!encryptedPassword) {
    throw new Error("Password encryption failed");
  }
  
  return encryptedPassword;
};