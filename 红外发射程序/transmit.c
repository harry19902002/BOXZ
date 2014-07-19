#include "reg51.h"
#include "intrins.h"

#define uint  unsigned int   //宏定义
#define uchar unsigned char

sfr AUXR = 0x8e;     //Auxiliary register
sbit HWTx=P3^5;     //位声明：红外发射管脚
sbit key=P3^2;
bit  HWTx_Out;      //红外发射管脚的状态
bit  Key_Flag,Flag;     //分别是：按键按下的标志位，定时器开始的标志位
uint Count,Set_Count;    //控制定时时间的变量
uchar Add;
uchar Data,HWTx_Code,HWTx_data;

void delay(uint z) //延时时间约为 1ms*X  晶振为12M
{
	unsigned char i, j;
		for(;z>0;z--){
			_nop_();
			_nop_();
			i = 12;
			j = 169;
			do
			{
				while (--j);
			} while (--i);
		}
}

void Key_Scan()
{
         uchar Key_Temp=0,i=0;
         if(key!=1)
				{
					delay(50);
					if(key!=1)
					{ 
						while(!key);
									Key_Flag=1;
           }
        } 
}

void Send_Code8()
{
         uchar i=0;   //循环变量
         for(i=0;i<8;i++) //8位数据，顾循环8次
        {
                 Set_Count=0x002b;//准备发送长达0.56ms(13*43=560us)的方波
                 Flag=1;    //置标志位(通过该标志位控制高低电平的转换)，因为高电平器件，38K的波形不会改变
					Count=0;  //清零，准备计数
					TR0=1;   //启动定时器
					while(Count<Set_Count); //定时0.56ms，先发送0.56ms的高电平
					TR0=0;    //关闭定时器

					if(HWTx_Code&0x80) //判断待发送的数据的最高位为1或0，
					{       //将决定低电平时间的长短，便于接收装置的接收
							Set_Count=130; //如果是1，则产生1.69ms(13*130=1690us)的低电平
					}
					else
					{
							Set_Count=43; //如果是0，则产生0.56ms(13*43=560us)的低电平
					}

					Flag=0;    //用于产生低电平的标志位
					Count=0;   //清零，准备计数
					TR0=1;    //启动定时器
					while(Count<Set_Count);//产生长达对应时间的低电平
					TR0=0;       //关闭定时器

				HWTx_Code=HWTx_Code<<1;//左移一位，准备发送  
			}
}


void Send_Code()
{
				uchar i=0;

        Set_Count=690;//准备产生9ms(13*690=9000)的高电平 
        Flag=1;    //置标志位，取反(该标志位控制高低电平的转换)
        Count=0;   //定时器计数
        TR0=1;    //启动定时器
        while(Count<Set_Count);//定时9ms，产生9ms的高电平
        TR0=0;   //关闭定时器

        Set_Count=340;//准备产生4.5ms(13*340=4500)的低电平
        Flag=0;    //置位，取反(该标志位控制高低电平的转换)
				Count=0;   //清零，准备计数
        TR0=1;    //启动定时器T0
        while(Count<Set_Count); //产生4.5ms的低电平
        TR0=0;        //关闭定时器
				
				HWTx_Code=Add;    //发送八位地址
				Send_Code8();
				
				HWTx_Code=~Add;    //发送八位地址反码
				Send_Code8();
				
				HWTx_Code=Data;      //发送八位数据
				Send_Code8();
				
				HWTx_Code=~Data;     //发送八位数据反码
				Send_Code8();

          Set_Count=34;//准备产生4.5ms(13*340=4500)的低电平
          Flag=1;    //置位，取反(该标志位控制高低电平的转换)
          Count=0;   //清零，准备计数
          TR0=1;    //启动定时器T0
          while(Count<Set_Count); //产生4.5ms的低电平
          TR0=0;

        HWTx=0;   //置发射引脚的状态为高电平
				delay(23);//延时23ms

        Set_Count=690; //准备产生9.12ms的低电平，作为结束标志
        Flag=1;
        Count=0;
        TR0=1;
         while(Count<Set_Count);//产生时间长达9.12ms的低电平
         TR0=0;       //关闭定时器

         Set_Count=170;//准备产生2.1ms的高电平，作为结束标志
         Flag=0;
         Count=0;
         TR0=1;
         while(Count<Set_Count);
         TR0=0;

		Set_Count=50; //准备产生一瞬间的低电平，作为结束标志
        Flag=1;
        Count=0;
        TR0=1;
         while(Count<Set_Count);//产生时间长达一瞬间的低电平
         TR0=0;       //关闭定时器
				 
				 HWTx=1;
}

void init_timer0()
{
		AUXR |= 0x80;		//定时器时钟1T模式
		TMOD &= 0xF0;		//设置定时器模式
		TL0 = 0x64;			//设置定时初值
		TH0 = 0xFF;			//设置定时初值
		TF0 = 0;				//清除TF0标志
		TR0 = 1;				//定时器0开始计时
    ET0 = 1;        //使能定时器0中断
    EA = 1;
}
// USER CODE END

void main(void)
{
       init_timer0(); //定时器T0的初始化函数
			 Add=0x00;
       Count=0;    //定时器计数值清零
       Flag=0;     //高低电平变化标志位
       HWTx_Out=0;    //发射引脚的状态

  while(1)
  {

           Key_Scan();    //按键扫描函数
          if(Key_Flag==1)   //按键按下的标志，是否置位
					{
						Data=0x30;
						Send_Code(); //发送数据
						delay(500);  //延时0.1s
						Key_Flag=0;  //按键按下标志位清零
          }
    }
}

void tm0_isr() interrupt 1 using 1 //定时器T0
{
          Count++;   //变量计数，可得出进入T0的次数，便可得出定时时间
          if(Flag==1)
         {
                  HWTx_Out=~HWTx_Out;  //取反，不断输出周期为26us的方波
         }
        else
        {
					HWTx_Out=0;  //这里决定在接受程序，是以高电平还是低电平作为判断1和0的依据
       }
        HWTx=HWTx_Out;  
}
// USER CODE END
