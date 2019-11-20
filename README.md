# HImagePicker
Android Image Picker Library, provides ease in picking image from android native interfaces of camera and gallery

### Android Module's Gradle Configuration
Add dependency in your app module's `build.gradle`</br>
```groovy
implementation 'com.hassanjamil.himagepicker:library:1.1'`
```
### Project/Root Level Gradle Dependency
Add maven url in you root project's `build.gradle`
```android
allprojects {
    repositories {
        maven { url "http://api.tplmaps.com:8081/artifactory/example-repo-local" }
    }
}
```
### Code Snippet
```android
ConfigIPicker config = new ConfigIPicker(MainActivity.this)
                .setLayoutDirection("rtl" | "ltr")  // Set picker dialog layout direction
                .setDialogTitle("Pick Image via")   // Set picker dialog title
                .setDialogStrCamera("Camera")       // Set picker dialog camera option string
                .setDialogStrGallery("Gallery")     // Set picker dialog's gallery option string
                .setSingleTrue()                    // Set single for single image selection or limit = 1
                .setCropModeTrue()       // Set crop mode true after single image selection from camera or gallery, default value is false
                .setFolderModeTrue()                // Set Folder mode of images for gallery, default value is false
                .setListener(this)                  // Set callback listener
                .setShowCamera(false)           
                .setDirPath(getCustomDirectoryPath(this));  // Pass custom directory path
HImagePicker.getInstance().config(config).load();   // Trigger picker dialog or start functionality
```
> To use the functionality you can get help from [app](https://github.com/hassaanjamil/HImagePicker/tree/master/app) module of the repo.</br>
Based on https://github.com/esafirm/android-image-picker
