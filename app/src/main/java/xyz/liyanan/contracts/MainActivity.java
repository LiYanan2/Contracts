package xyz.liyanan.contracts;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.liyanan.contracts.bean.Constract;
import xyz.liyanan.contracts.dao.ContractDao;
import xyz.liyanan.contracts.dao.OPenHelper;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, Object>> mapList;
    private OPenHelper oPenHelper;
    private List<Constract> constractList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        createData();
        initData();
        initView();
    }

    public void initData() {
        oPenHelper = new OPenHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = oPenHelper.getReadableDatabase();
        ContractDao contractDao = new ContractDao(sqLiteDatabase);
        constractList = contractDao.queryAll();
        mapList = new ArrayList<>();

        for (Constract constract : constractList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", constract.getName());
            map.put("phoneNum", constract.getPhoneNum());
            mapList.add(map);
        }
        sqLiteDatabase.close();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.contract_list);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, mapList, R.layout.layout,
                new String[]{"name", "phoneNum"}, new int[]{R.id.contract_name, R.id.phoneNum});
        listView.setAdapter(simpleAdapter);
        //长按某一条联系人，将提示是否删除该联系人
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int location = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("警告")
                        .setMessage("确定删除该联系人?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase sqLiteDatabase = oPenHelper.getReadableDatabase();
                                ContractDao contractDao = new ContractDao(sqLiteDatabase);
                                contractDao.delete(constractList.get(location));
                                sqLiteDatabase.close();
                                mapList.remove(location);
                                simpleAdapter.notifyDataSetInvalidated();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int location = position;
                TextView nameText = (TextView) view.findViewById(R.id.contract_name);
                final EditText phoneEdit = new EditText(MainActivity.this);
                //设置EditText的输入类型为string
                phoneEdit.setInputType(InputType.TYPE_CLASS_PHONE);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("修改" + nameText.getText().toString() + "的手机号")
                        .setView(phoneEdit)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String updatePhone = phoneEdit.getText().toString();
                                SQLiteDatabase sqLiteDatabase = oPenHelper.getReadableDatabase();
                                ContractDao contractDao = new ContractDao(sqLiteDatabase);
                                Constract constract = constractList.get(location);
                                constract.setPhoneNum(updatePhone);
                                contractDao.update(constract);
                                sqLiteDatabase.close();

                                Map<String, Object> updateMap = mapList.get(location);
                                updateMap.put("phoneNum", updatePhone);
                                mapList.remove(location);

                                mapList.add(location, updateMap);
                                simpleAdapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void createData() {
        OPenHelper oPenHelper = new OPenHelper(this);
        SQLiteDatabase sqLiteDatabase = oPenHelper.getReadableDatabase();
        ContractDao contractDao = new ContractDao(sqLiteDatabase);
        Constract constract = new Constract();

        for (int i = 0; i < 100; i++) {
            constract.setName("联系人" + i);
            constract.setPhoneNum("15958021736");
            contractDao.insert(constract);
        }
        sqLiteDatabase.close();
    }
}
