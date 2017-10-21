# README #

### What is this repository for? ###
This is a demonstration of cryptography that I made for the UNCG computer security club. The programs purpose is to illustrate how encryption is performed using Java. It can encrypt a string of text using either AES or DES and operates in either CBC (Cipher Block Chaining) or ECB (Electronic Codebook) modes.
* Version
1.00

### Background ###
#### Plaintext ####
Unencrypted information, whether it is data or text
#### Ciphertext ####
Encrypted information, whether it is data or text
#### Block Ciphers ####
Cryptographic algorithms, such as AES and DES, which operate on fixed sized blocks of bits and utilize fixed length keys.
#### Cipher Block Chaining (CBC) ####
Cipher mode developed in the 1976. It uses an Initialization Vector and XORs blocks of plaintext with the previous block's ciphertext. For more information look Wikipedia's article  on cipher block modes or click [here](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_.28CBC.29)
#### Electionic Codebook (ECB) ####
Simplest encryption mode. It takes in a block of plaintext and encrypts it using a key.For more information look Wikipedia's article  on cipher block modes or click [here](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Electronic_Codebook_.28ECB.29)

#### Cipher Block Chaining VS Electronic Codebook ####
| CBC | ECB |
| --- | --- |
| Slow Encryption | Fast Encryption |
| Fast Decryption | Fast Decryption |
| Hides patterns in plaintext | Plaintext data patterns in ciphertext |

### How do I get set up? ###
Very little needs to be done to set this up. Either download using an some VCS or IDE with a VCS system and open the project. All of the libraries are

### Contribution guidelines ###

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact
