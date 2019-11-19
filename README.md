# HImagePicker
Android Image Picker Library, provides ease in picking image from android native interfaces of camera and gallery

### Android Module's Gradle Configuration
Add dependency in your app module's `build.gradle`
`implementation 'com.hassanjamil.himagepicker:library:1.1'`

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
                .setLayoutDirection("rtl" | "ltr")
                .setDialogTitle("Pick Image via")
                .setDialogStrCamera("Camera")
                .setDialogStrGallery("Gallery")
                .setLayoutDirection("ltr")
                .setSingleTrue()
                .setCropModeTrue()
                .setFolderModeTrue()
                .setListener(this)
                .setShowCamera(false)
                .setDirPath(getCustomDirectoryPath(this));
HImagePicker.getInstance().config(config).load();
```
> To use the functionality you can get help from [app](https://github.com/hassaanjamil/HImagePicker/tree/master/app) module of the repo.</br>
Based on https://github.com/esafirm/android-image-picker
