## LineWallpaper

###Wallpaper

This app contains a live-wallpaper, which lays its main focus onto letting the user draw lines on his background.
These lines may be saved and deleted, or made to disappear again somehow after an certain time. Also they may be drawn in rainbow gradient and / or with balls at the end of the lines.  
Last but not least you can add a background picture and / or a clock.  
All these features are explained further now:

###Settings

This app lets the user create several settings for a livewallpaper from which exactly one is selected at a time.  
I made sure that it is by creating a setting initially (which is then selected) and making sure that the selected setting can not be deleted.  
We let the user change a setting by simply clicking onto the setting he wants to change in the listview.  
Also he may change a settings name, select a setting or delete one by long-clicking onto the wished setting and selecting the desired actiion in the upcoming AlertDialog.

####Possible specificated settings
The settings contain following:  

1. All new lines get the same color
  * if not they will receive a random color
  * the color will have to be specified by the user

2. All new lines are drawn in rainbow gradient
  * the steps in colors will have to be specified by the user (0 will disable the rainbow gradient)

3. Width of the drawn lines (__will affect also old lines__)

4. Saving and deletion of permanent lines

5. Drawing balls at the ends of the lines (__will also affect old lines__)

6. Making the lines disappear (only affects non-permanent lines; affects __ALL__ non-permanent lines)
  * __These settings will gain relevance as soon as the user stops drawing a line__ (_thus as soon as the user puts down a new finger on the screen a new line will be created_)
  * Make complete line disappear after certain time (uses "time until disappearance")
    * "time until disappearance" has to be defined
  * Make the complete line slowly fade out (with alpha; uses "time until action is perfromed")
  * Make the line being eaten from behind (point to point of the line is deleted; uses "time until action is perfromed")
    * "time until action is perfromed" has to be defined

7. Draw a picture as background
  * Picturepath has to be defined
  * Backgroundcolor is drawn when:
    * the background picture doesn't exist anymore
    * no background picture should be 

8. Draw a clock in the front of the background
  * x-position of the clock (relative to the leftover space -> between 0 and 1)
  * y-position of the clock (relative to the leftover space -> between 0 and 1)
  * diameter of the clock (relative to the smaller dimension -> between 0 and 1)
 
  1. Simple clock
    * alpha behind the clock (so the time may be read properly)
    * color of the hour index
    * color of the minute index
    * color of the second index
  2. Index-only clock (simple clock without frame -> only the indexes)
    * color of the hour index
    * color of the minute index
    * color of the second index
  3. Parabola clock (clock with parabolas as indexes)
    * alpha behind the clock (so the time may be read properly)
    * color of the parabola between the current hour and minute
    * color of the parabola between the current minute and second
  4. Digital clock
    * let the dots between the dots blink

9. Draw a visualizer in front of the background
  * x-position of the visualizer (relative to the leftover space -> between 0 and 1)
  * y-position of the visualizer (relative to the leftover space -> between 0 and 1)
  * diameter of the visualizer (relative to the smaller dimension -> between 0 and 1)
  * color of the responsive part of the visualizer
  * whether it's waveform-dependent or frequency-dependent
  
  1. Proximity visualizer
  2. Linear visualizer
