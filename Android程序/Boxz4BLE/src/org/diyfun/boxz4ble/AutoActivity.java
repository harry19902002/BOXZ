package org.diyfun.boxz4ble;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AutoActivity extends Activity {
    
    private TextView tvAuto;
    private TextView tvStart;
    private TextView tvConfig;
    private TextView tvHelp;
    
    ListView list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        initViews();
    }

    private void initViews() {
        
         list = (ListView) findViewById(R.id.lv_op);
         
         ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();  
         for(int i=0;i<5;i++)  
         {  
             HashMap<String, String> map = new HashMap<String, String>();  
//             map.put("ItemTitle", "This is Title.....");  
             map.put("ItemText", "This is text.....");  
             mylist.add(map);  
         }  
         //生成适配器，数组===》ListItem  
         SimpleAdapter mSchedule = new SimpleAdapter(this, //没什么解释  
                                                     mylist,//数据来源   
                                                     R.layout.auto_list_item,
                                                       
                                                     //动态数组与ListItem对应的子项          
                                                     new String[] { "ItemText"},   
                                                       
                                                     //ListItem的XML文件里面的两个TextView ID  
                                                     new int[] {R.id.tv_op});  
         //添加并且显示  
         list.setAdapter(mSchedule);  
        
//        tvAuto = (TextView)findViewById(R.id.tv_auto);
//        tvStart = (TextView)findViewById(R.id.tv_auto);
//        tvConfig = (TextView)findViewById(R.id.tv_auto);
//        tvConfig = (TextView)findViewById(R.id.tv_auto);
//        
//        tvAuto.setOnClickListener(new View.OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//                
//            }
//        });
//        
//        
        
        
    }

}
