#!/usr/bin/python 

import io # used to create file streams
import os 
import time
from datetime import datetime

def main():	
	dis_time = 1 # time in seconds
	recovery = 10  
	time_start_up = datetime.now().time()
	print('Start pH up test')
	os.system('sudo ./setGPIO 6 1')
	time.sleep(dis_time)
	os.system('sudo ./setGPIO 6 0')
	time_stop_up = datetime.now().time()
	print(time_start_up)
	print(time_stop_up)
	print('Start Time: ', time_start_up)
	print('Stop Time: ', time_stop_up)
	
	time.sleep(recovery)
	time_start_down = datetime.now().time()
	print('Start pH Down test')
	os.system('sudo ./setGPIO 13 1')
	time.sleep(dis_time)
	os.system('sudo ./setGPIO 13 0')
	time_stop_down = datetime.now().time()
	print(time_start_down)
	print(time_stop_down)
        
if __name__ == '__main__':
    main()
