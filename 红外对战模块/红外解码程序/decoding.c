#include <reg51.h>
#include <intrins.h>

#define uchar unsigned char
#define uint  unsigned int

void delay(uchar x);  		//x*0.14MS
void delayms(int ms);

sbit IRIN = P3^2;         //红外接收器数据线
sbit LED1 = P3^4;
sbit LED2 = P3^3;
sbit LED3 = P3^1;
sbit LED4 = P3^0;
int IR_val;

uchar receive_flag;

uchar IRCOM[7];

main()
{
//外部中断初始化
    INT0 = 1;
    IT0 = 1;                    //设置INT0的中断类型 (1:仅下降沿 0:上升沿和下降沿)
    EX0 = 1;                    //使能INT0中断
    EA = 1;
    
    IRIN=1;                    //I/O口初始化
		
    delayms(10);                //延时
		while(1)
		{
		if(receive_flag==1)	{
		receive_flag=0;
		switch(IRCOM[3])
			{
				case 0x20:
					LED1=0;
					delayms(1000);
				  LED1=1;
				break;
				case 0x30:
					LED2=0;
					delayms(1000);
					LED2=1;
				break;
				case 0x40:
					LED3=0;
				delayms(1000);
					LED3=1;
				case 0x50:
					LED4=1;
					delayms(1000);
					LED4=0;
				default:
					LED1=1;
					LED2=1;
				break;
			}
		}
		}
	
} //end main
/**********************************************************/
void exint0() interrupt 0      //INT0中断入口
{
  unsigned char j,k,N=0;
   EX0 = 0; 
	 delay(15);
	
	 if (IRIN==1)                         //确认IR信号出现
     { EX0 =1;
	   return;
	  }

	while (!IRIN)	                       
  {delay(1);}                          //等IR变为高电平，跳过9ms的前导低电平信号。
    

 for (j=0;j<4;j++)         //收集四组数据
 { 
  for (k=0;k<8;k++)        //每组数据有8位
  {
   while (IRIN)            //等 IR 变为低电平，跳过4.5ms的前导高电平信号。
     {delay(1);}
    while (!IRIN)          //等 IR 变为高电平
     {delay(1);}
     while (IRIN)           //计算IR高电平时长
      {
    delay(1);
    N++;           
    if (N>=30)
	 { EX0=1;
	 return;}                  //0.14ms计数过长自动离开。
      }                        //高电平计数完毕                
     IRCOM[j]=IRCOM[j] << 1;                  //数据最低位补“0”
     if (N>=8) {IRCOM[j] = IRCOM[j] | 0x01;}  //数据最低位补“1”
     N=0;
  }//end for k
 }//end for j

	if(IRCOM[0]!=0x80||IRCOM[1]!=0x0f||IRCOM[2]!=0x04)
	{	
		IRCOM[3]=0x00;
		EX0=1;
		LED2=~LED2;
		return;
		}	
	receive_flag=1;
   
//   if (IRCOM[2]!=~IRCOM[3])
//   { EX0=1;
//     return; }

//   IRCOM[5]=IRCOM[2] & 0x0F;     //取键码的低四位
//   IRCOM[6]=IRCOM[2] >> 4;       //右移4次，高四位变为低四位

//////////////////////////
//	LED=~LED;
//L1602_char(2,10,IRCOM[5]);
//L1602_char(2,9,IRCOM[6]);
     //beep();
//	 IR_val=IRCOM[5]+IRCOM[6]*16;
     EX0 = 1; 
} 


/**********************************************************/
void delay(unsigned char x)    //x*0.14MS
{
	for(;x>0;x--){
		unsigned char i, j;
		i = 2;
		j = 159;
		do
		{
			while (--j);
		} 
		while (--i);
	}
}

/**********************************************************/
void delayms(int ms)
{
	unsigned char i, j,time;
	for(time=ms;time>0;time--){
		i = 12;
		j = 169;
		do
		{
			while (--j);
		} while (--i);
	}
}

