# TastyToast
**Note:** Still WIP!

Contextual toasts that stick to an activity. There are three available flavors of toasts (which you could extend and customize as well) - Alert Toast, Confirmation Toast & Message Toasts.

##Gradle Dependency

Add this in your project app's ```build.gradle```

```sh
dependencies{
    compile 'com.greycellofp:tastytoast:1.0.2'
}
```

### Version
1.0.2

#Quick Usage
  - **Message Toasts**
```java
    TastyToast.makeText(YourActivty.this, "your message text", TastyToast.STYLE_MESSAGE).show();
```
![message demo](https://raw.githubusercontent.com/obipawan/TastyToast/master/message_snapshot.png)

  - **Alert Toasts**
```java
TastyToast.makeText(YourActivity.this, "some alert text", TastyToast.STYLE_ALERT).show();
```
![alert demo](https://raw.githubusercontent.com/obipawan/TastyToast/master/alert_snapshot.png)

  - **Confirmation Toasts**
```java
TastyToast.makeText(YourActivity.this, "some confirmation text", TastyToast.STYLE_CONFIRM).show();
```
![confirmation demo](https://raw.githubusercontent.com/obipawan/TastyToast/master/confirm_snapshot.png)

  - **Variable duration**
```java
//sets a variable duration based on the text size with time boundaries at 3sec <= t <= 10sec
TastyToast.makeText(YourActivity.this, "a long text ~ more than 120 characters", TastyToast.STYLE_MESSAGE).enableVariableDuration().show();
```
  - **Swipe to dismiss**
```java
//uses Jake Wharton's SwipeDismissTouchListener
TastyToast.makeText(YourActivity.this, "Some text", TastyToast.STYLE_MESSAGE).enableSwipeDismiss().show();
```
  - **Styling**
  
  You can customise your `TastyToast` by providing a `Style` input, or you can provide your own custom views like so 
```java 
makeText(Activity context, CharSequence text, Style style, int layoutId)
```
```java 
makeText(Activity context, CharSequence text, Style style, View customView)
```
  - **Text Size**
```java
float textSize = 20; //you should of course consider converting to sp
TastyToast.makeText(YourActivity.this, "Some text", TastyToast.STYLE_MESSAGE, textSize).show();
```

  - **In/Out Animations**
```java
TastyToast.Style style = TastyToast.STYLE_MESSAGE;
Animation inAnimation; //set your custom In animation
Animation outAnimation; //set your custom Out animation
style.setOutAnimation(outAnimation);
style.setInAnimation(inAnimation);
TastyToast.makeText(YourActivty.this, "your message text", style).show();
```

### Development

PRs highly appreciated

License
----
Public License
