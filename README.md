ValueBar
========

A beautiful Android custom View that works similar to a range or seekbar. Selection by gesture. With animations. **Supporting API level 11+.**

![alt tag](https://raw.github.com/PhilJay/ValueBar/master/screenshots/adgraphic.jpg)


Usage
========

For using `ValueBar`, either 

- **clone this repository** and add the library folder to your Android application project
- or download the **latest .jar file** from the [**release-section**](https://github.com/PhilJay/ValueBar/releases) and copy it into the `libs` folder of your Android application project.

Create a `ValueBar` in .xml:

```xml
<com.philjay.valuebar.ValueBar
        android:id="@+id/valueBar"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:layout_margin="5dp" />

```

or in code, and then add it to a layout:

```java
 ValueBar bar = new ValueBar(Context);
```

Apply styling, and display values:

```java
 bar.setMinMax(0, 1000);
 bar.setInterval(1f); // interval in which can be selected
 bar.setDrawBorder(false);
 bar.setValueTextSize(14f);
 bar.setMinMaxTextSize(14f);
 bar.setValueTextTypeface(...);
 bar.setMinMaxTextTypeface(...);
 
 // create your custom color formatter by using the BarColorFormatter interface
 bar.setColorFormatter(new RedToGreenFormatter());
 
 // add your custom text formatter by using the ValueTextFormatter interface
 bar.setValueTextFormatter(...);
            
 bar.setValue(800f); // display a value
            
 bar.setValueBarSelectionListener(...); // add a listener for callbacks when touching

```
