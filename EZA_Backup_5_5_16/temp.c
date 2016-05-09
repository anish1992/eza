/*
 
 Auther : 	EZ Aquarium
 date : 	01/29/2016
 Title:     temp.c
 Problem:   testing temprature senser
 resorces : http://www.raspberrypi-spy.co.uk/2013/03/raspberry-pi-1-wire-digital-thermometer-sensor/
 Class:                  ENGR 697
 
 Raspberry configuration procedure
 Update the /boot/config.txt file and add the following line
 
 dtoverlay=w1-gpoi,gpiopin=4	#if a pin other then gpio 4 is used
								# modify with appopreate pin
 
 After modifying config.txt, reboot system 
 sudo reboot
 
 This code relies on two kernel modules that must be loaded by the 
   following commands before the code is run:
 
 sudo modprobe w1-gpio
 sudo modprobe w1-therm
 
 These modules make it possible to access the 1-wire sensor via the Linux file system.
 
 Now find the 1-wire devices by using the following command
 cd /sys/bus/w1/devices
 ls 
 
 Connections
 
 Looking at the flat side of DS18B20, connect it to the Raspberry Pi 2 B
  as follows.
 
 DS18B20 Pin   Raspberry Pi 2 (Rev. B)
 Black         GND (P1 pin 39)
 White         GPIO4 (w/ 4.7k pull-up resistor) (P1 pin 7)
 Red	       3V3 (P1 pin 1)

 */
#include <stdio.h>
#include <dirent.h>
#include <string.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>

int main (void) {
    //system("sudo modprobe w1-gpio");
    //system("sudo modprobe w1-therm");
    DIR *dir;
    struct dirent *dirent;
    char dev[16];      // Dev ID
    char devPath[128]; // Path to device
    char buf[256];     // Data from device
    char tmpData[6];   // Temp C * 1000 reported by device
    char path[] = "/sys/bus/w1/devices";
    ssize_t numRead;
    
    dir = opendir (path);
    if (dir != NULL)
    {
        while ((dirent = readdir (dir)))
            // 1-wire devices are links beginning with 28-
            if (dirent->d_type == DT_LNK &&
                strstr(dirent->d_name, "28-") != NULL) {
                strcpy(dev, dirent->d_name);
                //printf("\nDevice: %s\n", dev);
            }
        (void) closedir (dir);
    }
    else
    {
        perror ("Couldn't open the w1 devices directory");
        return 1;
    }
    
    // Assemble path to OneWire device
    sprintf(devPath, "%s/%s/w1_slave", path, dev);
    // Read temp continuously
    // Opening the device's file triggers new reading
    //while(1) {
        int fd = open(devPath, O_RDONLY);
        if(fd == -1)
        {
            perror ("Couldn't open the w1 device.");
            return 1;
        }
        while((numRead = read(fd, buf, 256)) > 0)
        {
            strncpy(tmpData, strstr(buf, "t=") + 2, 5);
            float tempC = strtof(tmpData, NULL);
            //printf("Device: %s  - ", dev);
            //printf("Temp: %.3f C  ", tempC / 1000);
            printf("%.3f", (tempC / 1000) * 9 / 5 + 32);
        }
        close(fd);
    //} 
    /* return 0; --never called due to loop */
}
