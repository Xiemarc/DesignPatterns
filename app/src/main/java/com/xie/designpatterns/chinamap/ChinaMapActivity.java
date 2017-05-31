package com.xie.designpatterns.chinamap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xie.designpatterns.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * svg实现地图
 * Created by marc on 2017/5/4.
 */

public class ChinaMapActivity extends AppCompatActivity {
    private ChinaMapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinamap);
        mapView = (ChinaMapView) findViewById(R.id.view_map);
    }

    /**
     * 异步加载svg地图数据
     * @param view
     */
    public void loadMapData(View view) {
        parseDemoData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dataList -> mapView.setData(dataList),
                        throwable -> Toast.makeText(getApplicationContext(), "加载演示数据失败！", Toast.LENGTH_SHORT).show()//onError错误
                );
    }

    /**
     * 清除预设数据
     */
    public void clearMapData(View view) {
        mapView.setData(null);
    }

    /**
     * 异步解析演示数据
     *
     * @return
     */
    private Observable<List<ProvinceData>> parseDemoData() {
        Observable<List<ProvinceData>> observable = Observable.create(subscriber -> {
            List<ProvinceData> demoDataList = new ArrayList<>();
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("demo.json"), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();

                JSONObject root = new JSONObject(builder.toString());
                JSONArray dataList = root.optJSONArray("province");
                if (dataList != null && dataList.length() != 0) {
                    for (int i = 0; i < dataList.length(); i++) {
                        ProvinceData item = new ProvinceData();
                        JSONObject obj = dataList.optJSONObject(i);
                        item.setNumber(obj.optInt("number"));
                        item.setProvinceId(obj.optInt("provinceName"));
                        item.setProvinceId(obj.optInt("provinceId"));
                        demoDataList.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            subscriber.onNext(demoDataList);
            subscriber.onCompleted();
        });
        return observable;
    }

}
