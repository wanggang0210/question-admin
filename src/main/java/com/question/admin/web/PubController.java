package com.question.admin.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.question.admin.config.props.WeatherProperties;
import com.question.admin.utils.JedisUtils;
import com.question.admin.constant.Constants;
import com.question.admin.domain.vo.Result;
import com.question.admin.domain.vo.manager.pub.WeatherDetailVo;
import com.question.admin.domain.vo.manager.pub.WeatherVo;
import com.question.admin.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PubController {

    @Autowired
    WeatherProperties weatherProperties;

    @Autowired
    JedisUtils jedisUtils;

    /**
     *
     * @return
     */
    @RequestMapping(value="/weather",method = {RequestMethod.GET})
    public Result weather(){

        WeatherVo weatherVo;
        String strCityId= "101280601";
        String weatherCacheKey= "weather_" + strCityId;
        if(jedisUtils.exists(weatherCacheKey)){
            weatherVo = (WeatherVo)jedisUtils.getObject(weatherCacheKey);
        }else{
            Map<String,String> params= new HashMap<>();
            params.put("appid",weatherProperties.getAppId());
            params.put("appsecret",weatherProperties.getAppSecret());
            params.put("version","v1");  //固定值v1
            params.put("cityid",strCityId);
            String httpResult = HttpClientUtils.get("https://www.tianqiapi.com/api/",params);

            weatherVo= new WeatherVo();
            WeatherDetailVo weatherEntity;
            List<WeatherDetailVo> weatherDetailList= new ArrayList<>();

            JSONObject response=JSONObject.parseObject(httpResult);
            JSONArray data=response.getJSONArray("data");

            Long cityId=response.getLong("cityid");
            String province=response.getString("province");
            String city=response.getString("city");

            JSONObject tempObject;
            for(Object object:data){
                weatherEntity = new WeatherDetailVo();
                tempObject = ((JSONObject)object);
                weatherEntity.setDate(tempObject.getDate("date"));
                weatherEntity.setWeek(tempObject.getString("week"));
                weatherEntity.setWea(tempObject.getString("wea"));
                weatherEntity.setWeaImg(tempObject.getString("wea_img"));
                weatherEntity.setTem1(tempObject.getString("tem1"));
                weatherEntity.setTem2(tempObject.getString("tem2"));
                weatherDetailList.add(weatherEntity);
            }

            weatherVo.setCityId(cityId);
            weatherVo.setProvince(province);
            weatherVo.setCity(city);
            weatherVo.setDateList(weatherDetailList);

            //缓存
            jedisUtils.saveObject(weatherCacheKey, weatherVo, Constants.ExpireTime.THREE_HOURS);
        }
        Result result = new Result(true,weatherVo);
        return result;
    }
}
