# Mobile and application development Android project

## Explain how you ensure user is the right one starting the app

When a user launches the app, he must sign in by using his/her personal credentials which are an email address and a password. There are securely stored in the gradle.proprieties file in SHA-256. The string names are also obfuscated under MyVar1 and MyVar2.

## How do you securely save user's data on your phone ?

I store user's data in an encrypted file stored in the device's internal storage by using EncryptedFile Builder. It has several advantages. First, only the app which generates this encrypted file can access to it because this file is sandboxed. Moreover, as an added security measure, when the user uninstalls the app, the device deletes the file that the app saved within internal storage.

## How did you hide the API url ?

I choose to store in Base64 the API url in the gradle.proprieties file, and I obfuscate the string name under MyVar5.

```java
MyVar5="....."
```
Indeed, this file is not recoverable when someone tries to decompile the .apk archive. 

Then, in order to access the url from Java, I add it as a buildConfigField in build.gradle file.

```java
buildTypes {
        debug{
            buildConfigField 'String', "Var5", MyVar5
        }
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            buildConfigField 'String', "Var5", MyVar5
        }
    }
```

## Screenshots of your application

### Homescreen

![Screenshot](/screenshots/image1.PNG)

### Connected to the Internet check

![Screenshot](/screenshots/image2.PNG)

### Not connected to the Internet

![Screenshot](/screenshots/image3.PNG)

### Display the data

![Screenshot](/screenshots/image4.PNG)

### Refresh and log out menu

![Screenshot](/screenshots/image5.PNG)