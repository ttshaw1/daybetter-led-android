Combines https://github.com/kquinsland/JACKYLED-BLE-RGB-LED-Strip-controller with the Daybetter LED protocol in https://github.com/shindekokoro/homebridge-daybetter/tree/master for bare-bones dead-simple control of Daybetter 12V LED strip controllers. No guarantees it'll work for you. 

I couldn't install the APK from JackyLED; probably some Android security thing. Maybe it would work with ADB. But I'm not going to provide an APK. I recommend compiling with the gradle version specified in which of the project files and Java 11. You probably know more about Android compilation than me. And if not, I can't help you, because I know basically nothing about Android app compilation.

The most annoying limitation is that I can't set the RGB values directly. Entered RGB values are used to select one of 8 colors. Maybe further reverse-engineering the official app and looking at its protocol would help, but I'm not going to spend time on it. But if you do, let me know what you find and I can edit this app to add functionality.