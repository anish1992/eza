#include <stdio.h>
#include <pigpio.h>
#include <unistd.h>

/*
Assuming first input argument being the gpio number
and second argument being 1/0 indicating on/off. 
and third argument is wait time in uses for ph motors* 
*/
int main(int argc, char *argv[]){
	int gpio_number;
	int state;
	int wait;
	
	if(gpioInitialise() < 0){
		printf("Initialization failed\n");
		return 1;
	}
	sscanf(argv[1],"%d",&gpio_number);
	sscanf(argv[2],"%d",&state);
	sscanf(argv[3],"%d",&wait);

	//set gpio number to be output
	gpioSetMode(gpio_number, PI_OUTPUT);	
		
	//write output
	//Run motors for wait time Motor
	if (((gpio_number==5)||(gpio_number==21))&&(state==1)){
		gpioWrite(gpio_number, 1);
		usleep(wait);
		gpioWrite(gpio_number, 0); 
	} else {
		gpioWrite(gpio_number, state);
	}
	return 0;
}
