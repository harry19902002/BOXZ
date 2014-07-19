#include "reg51.h"
#include "intrins.h"

#define uint  unsigned int   //�궨��
#define uchar unsigned char

sfr AUXR = 0x8e;     //Auxiliary register
sbit HWTx=P3^5;     //λ���������ⷢ��ܽ�
sbit key=P3^2;
bit  HWTx_Out;      //���ⷢ��ܽŵ�״̬
bit  Key_Flag,Flag;     //�ֱ��ǣ��������µı�־λ����ʱ����ʼ�ı�־λ
uint Count,Set_Count;    //���ƶ�ʱʱ��ı���
uchar Add;
uchar Data,HWTx_Code,HWTx_data;

void delay(uint z) //��ʱʱ��ԼΪ 1ms*X  ����Ϊ12M
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
         uchar i=0;   //ѭ������
         for(i=0;i<8;i++) //8λ���ݣ���ѭ��8��
        {
                 Set_Count=0x002b;//׼�����ͳ���0.56ms(13*43=560us)�ķ���
                 Flag=1;    //�ñ�־λ(ͨ���ñ�־λ���Ƹߵ͵�ƽ��ת��)����Ϊ�ߵ�ƽ������38K�Ĳ��β���ı�
					Count=0;  //���㣬׼������
					TR0=1;   //������ʱ��
					while(Count<Set_Count); //��ʱ0.56ms���ȷ���0.56ms�ĸߵ�ƽ
					TR0=0;    //�رն�ʱ��

					if(HWTx_Code&0x80) //�жϴ����͵����ݵ����λΪ1��0��
					{       //�������͵�ƽʱ��ĳ��̣����ڽ���װ�õĽ���
							Set_Count=130; //�����1�������1.69ms(13*130=1690us)�ĵ͵�ƽ
					}
					else
					{
							Set_Count=43; //�����0�������0.56ms(13*43=560us)�ĵ͵�ƽ
					}

					Flag=0;    //���ڲ����͵�ƽ�ı�־λ
					Count=0;   //���㣬׼������
					TR0=1;    //������ʱ��
					while(Count<Set_Count);//���������Ӧʱ��ĵ͵�ƽ
					TR0=0;       //�رն�ʱ��

				HWTx_Code=HWTx_Code<<1;//����һλ��׼������  
			}
}


void Send_Code()
{
				uchar i=0;

        Set_Count=690;//׼������9ms(13*690=9000)�ĸߵ�ƽ 
        Flag=1;    //�ñ�־λ��ȡ��(�ñ�־λ���Ƹߵ͵�ƽ��ת��)
        Count=0;   //��ʱ������
        TR0=1;    //������ʱ��
        while(Count<Set_Count);//��ʱ9ms������9ms�ĸߵ�ƽ
        TR0=0;   //�رն�ʱ��

        Set_Count=340;//׼������4.5ms(13*340=4500)�ĵ͵�ƽ
        Flag=0;    //��λ��ȡ��(�ñ�־λ���Ƹߵ͵�ƽ��ת��)
				Count=0;   //���㣬׼������
        TR0=1;    //������ʱ��T0
        while(Count<Set_Count); //����4.5ms�ĵ͵�ƽ
        TR0=0;        //�رն�ʱ��
				
				HWTx_Code=Add;    //���Ͱ�λ��ַ
				Send_Code8();
				
				HWTx_Code=~Add;    //���Ͱ�λ��ַ����
				Send_Code8();
				
				HWTx_Code=Data;      //���Ͱ�λ����
				Send_Code8();
				
				HWTx_Code=~Data;     //���Ͱ�λ���ݷ���
				Send_Code8();

          Set_Count=34;//׼������4.5ms(13*340=4500)�ĵ͵�ƽ
          Flag=1;    //��λ��ȡ��(�ñ�־λ���Ƹߵ͵�ƽ��ת��)
          Count=0;   //���㣬׼������
          TR0=1;    //������ʱ��T0
          while(Count<Set_Count); //����4.5ms�ĵ͵�ƽ
          TR0=0;

        HWTx=0;   //�÷������ŵ�״̬Ϊ�ߵ�ƽ
				delay(23);//��ʱ23ms

        Set_Count=690; //׼������9.12ms�ĵ͵�ƽ����Ϊ������־
        Flag=1;
        Count=0;
        TR0=1;
         while(Count<Set_Count);//����ʱ�䳤��9.12ms�ĵ͵�ƽ
         TR0=0;       //�رն�ʱ��

         Set_Count=170;//׼������2.1ms�ĸߵ�ƽ����Ϊ������־
         Flag=0;
         Count=0;
         TR0=1;
         while(Count<Set_Count);
         TR0=0;

		Set_Count=50; //׼������һ˲��ĵ͵�ƽ����Ϊ������־
        Flag=1;
        Count=0;
        TR0=1;
         while(Count<Set_Count);//����ʱ�䳤��һ˲��ĵ͵�ƽ
         TR0=0;       //�رն�ʱ��
				 
				 HWTx=1;
}

void init_timer0()
{
		AUXR |= 0x80;		//��ʱ��ʱ��1Tģʽ
		TMOD &= 0xF0;		//���ö�ʱ��ģʽ
		TL0 = 0x64;			//���ö�ʱ��ֵ
		TH0 = 0xFF;			//���ö�ʱ��ֵ
		TF0 = 0;				//���TF0��־
		TR0 = 1;				//��ʱ��0��ʼ��ʱ
    ET0 = 1;        //ʹ�ܶ�ʱ��0�ж�
    EA = 1;
}
// USER CODE END

void main(void)
{
       init_timer0(); //��ʱ��T0�ĳ�ʼ������
			 Add=0x00;
       Count=0;    //��ʱ������ֵ����
       Flag=0;     //�ߵ͵�ƽ�仯��־λ
       HWTx_Out=0;    //�������ŵ�״̬

  while(1)
  {

           Key_Scan();    //����ɨ�躯��
          if(Key_Flag==1)   //�������µı�־���Ƿ���λ
					{
						Data=0x30;
						Send_Code(); //��������
						delay(500);  //��ʱ0.1s
						Key_Flag=0;  //�������±�־λ����
          }
    }
}

void tm0_isr() interrupt 1 using 1 //��ʱ��T0
{
          Count++;   //�����������ɵó�����T0�Ĵ�������ɵó���ʱʱ��
          if(Flag==1)
         {
                  HWTx_Out=~HWTx_Out;  //ȡ���������������Ϊ26us�ķ���
         }
        else
        {
					HWTx_Out=0;  //��������ڽ��ܳ������Ըߵ�ƽ���ǵ͵�ƽ��Ϊ�ж�1��0������
       }
        HWTx=HWTx_Out;  
}
// USER CODE END
