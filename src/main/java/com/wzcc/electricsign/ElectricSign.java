package com.wzcc.electricsign;

import com.alibaba.fastjson.JSONObject;
import com.timvale.sealplatform.bean.PosBean;
import com.timvale.sealplatform.result.SealDetailResult;
import com.timvale.sealplatform.result.VerifySignResult;
import com.timvale.sealplatform.result.file.SignOFDResult;
import com.timvale.sealplatform.result.file.SignPDFResult;
import com.timvale.sealplatform.sdk.file.SignLocalClient;

public class ElectricSign {

    PosBean posBean=new PosBean();
    SignLocalClient signLocalClient;

    //region 初始化
    public ElectricSign(String AppId,String Secret,String Protocol,String DomainName){
        signLocalClient=SignHelper.getSignLocalClient(AppId,Secret,Protocol,DomainName);
        posBean.setPosPage("1");
        posBean.setPosX(100);
        posBean.setPosY(100);
    }
    public ElectricSign(String jsonString){
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        signLocalClient=SignHelper.getSignLocalClient(jsonObject.getString("AppId"),
                jsonObject.getString("Secret"),
                jsonObject.getString("Protocol"),
                jsonObject.getString("DomainName")
        );
        posBean.setPosPage("1");
        posBean.setPosX(100);
        posBean.setPosY(100);
    }
    //endregion

    //region PosBean初始化
    public void setPosType(String PosType){
        posBean.setPosType(PosType);
    }
    public void setPosPage(String PosPage){
        posBean.setPosPage(PosPage);
    }
    public void setPosX(int PosX){
        posBean.setPosX(PosX);
    }
    public void setPosY(int PosY){
        posBean.setPosY(PosY);
    }
    public void setKey(String Key){
        posBean.setKey(Key);
    }
    public void setKeyIndex(int KeyIndex){
        posBean.setKeyIndex(KeyIndex);
    }
    public void setAddSignTime(boolean AddSignTime){
        posBean.setAddSignTime(AddSignTime);
    }
    public void SetPosBean(String jsonString){
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        if(jsonObject.getString("PosType")!=null)
            posBean.setPosType(jsonObject.getString("PosType"));
        if(jsonObject.getString("PosPage")!=null)
            posBean.setPosPage(jsonObject.getString("PosPage"));
        if(jsonObject.getInteger("PosX")!=null)
            posBean.setPosX(jsonObject.getInteger("PosX"));
        if(jsonObject.getInteger("PosY")!=null)
            posBean.setPosY(jsonObject.getInteger("PosY"));
        if(jsonObject.getString("Key")!=null)
            posBean.setKey(jsonObject.getString("Key"));
        if(jsonObject.getInteger("KeyIndex")!=null)
            posBean.setKeyIndex(jsonObject.getInteger("KeyIndex"));
        if(jsonObject.getBoolean("AddSignTime")!=null)
            posBean.setAddSignTime(jsonObject.getBoolean("AddSignTime"));
    }
    //endregion

    //region PDF
    public JSONObject SignPDF(String srcPdfFile,String dstPdfFile,String sealSn,int signType,String password){
        JSONObject res=new JSONObject();
        SignPDFResult signPDFResult = signLocalClient.localSignPDF(srcPdfFile, dstPdfFile,sealSn, signType, posBean, password);

        res.put("SignLogId",signPDFResult.getSignLogId());
        res.put("ErrCode",signPDFResult.getErrCode());
        res.put("ErrShow",signPDFResult.isErrShow());
        res.put("Msg",signPDFResult.getMsg());
        return res;
    }
    public JSONObject SignPDF(byte[] srcBytes,String sealSn,int signType,String password){
        JSONObject res=new JSONObject();
        SignPDFResult signPDFResult = signLocalClient.localSignPDF(srcBytes,sealSn, signType, posBean, password);

        res.put("SignLogId",signPDFResult.getSignLogId());
        res.put("OutByte",signPDFResult.getOutByte());
        res.put("ErrCode",signPDFResult.getErrCode());
        res.put("ErrShow",signPDFResult.isErrShow());
        res.put("Msg",signPDFResult.getMsg());
        return res;
    }
    public JSONObject SignPDF(String jsonString){
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        JSONObject res=new JSONObject();
        SignPDFResult signPDFResult;
        boolean err=false;

        if ((jsonObject.getString("srcPdfFile")==null||jsonObject.getString("dstPdfFile")==null)&&jsonObject.getBytes("srcBytes")==null){
            res.put("InputErr",1);
            res.put("Msg","文件地址及文件流为空！");
            err=true;
        }
        if(jsonObject.getString("sealSn")==null){
            res.put("InputErr",1);
            res.put("Msg","sealSn为空！");
            err=true;
        }
        if (err)
            return res;

        if (jsonObject.getBytes("srcBytes")==null) {
            signPDFResult= signLocalClient.localSignPDF(
                    jsonObject.getString("srcPdfFile"),
                    jsonObject.getString("dstPdfFile"),
                    jsonObject.getString("sealSn"),
                    jsonObject.getInteger("signType") == null ? 1 : jsonObject.getInteger("signType"),
                    posBean,
                    jsonObject.getString("password") == null ? "" : jsonObject.getString("password")
            );
        }else {
            signPDFResult = signLocalClient.localSignPDF(
                    jsonObject.getBytes("srcBytes"),
                    jsonObject.getString("sealSn"),
                    jsonObject.getInteger("signType") == null ? 1 : jsonObject.getInteger("signType"),
                    posBean,
                    jsonObject.getString("password") == null ? "" : jsonObject.getString("password")
            );
        }
        res.put("SignLogId",signPDFResult.getSignLogId());
        res.put("OutByte",signPDFResult.getOutByte());
        res.put("ErrCode",signPDFResult.getErrCode());
        res.put("ErrShow",signPDFResult.isErrShow());
        res.put("Msg",signPDFResult.getMsg());
        return res;
    }

    public JSONObject VerifyPDF(String srcPdfFile, String password){
        VerifySignResult verifySignResult=signLocalClient.verifySignedFile(srcPdfFile,password).get(0);
        JSONObject res=new JSONObject();
        res.put("ErrCode",verifySignResult.getErrCode());
        res.put("Msg",verifySignResult.getMsg());
        res.put("ErrShow",verifySignResult.isErrShow());
        res.put("VerifyResult",verifySignResult.isVerifyResult());
        res.put("VerifyCode",verifySignResult.getVerifyCode());
        res.put("VerifyMsg",verifySignResult.getVerifyMsg());
        return res;
    }
    public JSONObject VerifyPDF(String jsonString){
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        JSONObject res=new JSONObject();
        if(jsonObject.getString("srcPdfFile")==null){
            res.put("InputErr",1);
            res.put("Msg","srcPdfFile为空！");
            return res;
        }
        VerifySignResult verifySignResult=signLocalClient.verifySignedFile(
                jsonObject.getString("srcPdfFile"),
                jsonObject.getString("password")
        ).get(0);

        res.put("ErrCode",verifySignResult.getErrCode());
        res.put("Msg",verifySignResult.getMsg());
        res.put("ErrShow",verifySignResult.isErrShow());
        res.put("VerifyResult",verifySignResult.isVerifyResult());
        res.put("VerifyCode",verifySignResult.getVerifyCode());
        res.put("VerifyMsg",verifySignResult.getVerifyMsg());
        return res;
    }
    //endregion

    //region OFD
    public JSONObject SignOFD(String srcPdfFile,String dstPdfFile,String sealSn,int signType,String password){
        JSONObject res=new JSONObject();
        SignOFDResult signOFDResult = signLocalClient.localSignOFD(srcPdfFile, dstPdfFile,sealSn, signType, posBean, password);

        res.put("SignLogId",signOFDResult.getSignLogId());
        res.put("OutByte",signOFDResult.getOutByte());
        res.put("ErrCode",signOFDResult.getErrCode());
        res.put("ErrShow",signOFDResult.isErrShow());
        res.put("Msg",signOFDResult.getMsg());
        return res;
    }
    public JSONObject SignOFD(byte[] srcBytes,String sealSn,int signType,String password){
        JSONObject res=new JSONObject();
        SignOFDResult signOFDResult = signLocalClient.localSignOFD(srcBytes,sealSn, signType, posBean, password);

        res.put("SignLogId",signOFDResult.getSignLogId());
        res.put("OutByte",signOFDResult.getOutByte());
        res.put("ErrCode",signOFDResult.getErrCode());
        res.put("ErrShow",signOFDResult.isErrShow());
        res.put("Msg",signOFDResult.getMsg());
        return res;
    }
    public JSONObject SignOFD(String jsonString){
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        JSONObject res=new JSONObject();
        SignOFDResult signOFDResult;
        boolean err=false;

        if ((jsonObject.getString("srcPdfFile")==null||jsonObject.getString("dstPdfFile")==null)&&jsonObject.getBytes("srcBytes")==null){
            res.put("InputErr",1);
            res.put("Msg","文件地址及文件流为空！");
            err=true;
        }
        if(jsonObject.getString("sealSn")==null){
            res.put("InputErr",1);
            res.put("Msg","sealSn为空！");
            err=true;
        }
        if (err)
            return res;

        if (jsonObject.getBytes("srcBytes")==null) {
            signOFDResult= signLocalClient.localSignOFD(
                    jsonObject.getString("srcPdfFile"),
                    jsonObject.getString("dstPdfFile"),
                    jsonObject.getString("sealSn"),
                    jsonObject.getInteger("signType") == null ? 1 : jsonObject.getInteger("signType"),
                    posBean,
                    jsonObject.getString("password") == null ? "" : jsonObject.getString("password")
            );
        }else {
            signOFDResult = signLocalClient.localSignOFD(
                    jsonObject.getBytes("srcBytes"),
                    jsonObject.getString("sealSn"),
                    jsonObject.getInteger("signType") == null ? 1 : jsonObject.getInteger("signType"),
                    posBean,
                    jsonObject.getString("password") == null ? "" : jsonObject.getString("password")
            );
        }
        res.put("SignLogId",signOFDResult.getSignLogId());
        res.put("OutByte",signOFDResult.getOutByte());
        res.put("ErrCode",signOFDResult.getErrCode());
        res.put("ErrShow",signOFDResult.isErrShow());
        res.put("Msg",signOFDResult.getMsg());
        return res;
    }

    public JSONObject VerifyOFD(String srcPdfFile, String password){
        VerifySignResult verifySignResult=signLocalClient.verifyOFDSignedFile(srcPdfFile,password).get(0);
        JSONObject res=new JSONObject();
        res.put("ErrCode",verifySignResult.getErrCode());
        res.put("Msg",verifySignResult.getMsg());
        res.put("ErrShow",verifySignResult.isErrShow());
        res.put("VerifyResult",verifySignResult.isVerifyResult());
        res.put("VerifyCode",verifySignResult.getVerifyCode());
        res.put("VerifyMsg",verifySignResult.getVerifyMsg());
        return res;
    }
    public JSONObject VerifyOFD(String jsonString){
        JSONObject jsonObject=JSONObject.parseObject(jsonString);
        JSONObject res=new JSONObject();
        if(jsonObject.getString("srcPdfFile")==null){
            res.put("InputErr",1);
            res.put("Msg","srcPdfFile为空！");
            return res;
        }
        VerifySignResult verifySignResult=signLocalClient.verifyOFDSignedFile(
                jsonObject.getString("srcPdfFile"),
                jsonObject.getString("password")
        ).get(0);

        res.put("ErrCode",verifySignResult.getErrCode());
        res.put("Msg",verifySignResult.getMsg());
        res.put("ErrShow",verifySignResult.isErrShow());
        res.put("VerifyResult",verifySignResult.isVerifyResult());
        res.put("VerifyCode",verifySignResult.getVerifyCode());
        res.put("VerifyMsg",verifySignResult.getVerifyMsg());
        return res;
    }
    //endregion

    //region 获取印章
    public JSONObject GetSign(String sealSn){
        SealDetailResult PicB64 =  signLocalClient.getSealDetail(sealSn);
        JSONObject res=new JSONObject();
        res.put("ErrCode",PicB64.getErrCode());
        res.put("Msg",PicB64.getMsg());
        res.put("ErrShow",PicB64.isErrShow());
        res.put("PicB64",PicB64.getPicB64());
        res.put("Name",PicB64.getName());
        res.put("SealId",PicB64.getSealId());
        res.put("Data",PicB64.getData());
        res.put("CertB64",PicB64.getCertB64());
        res.put("Width",PicB64.getWidth());
        res.put("Height",PicB64.getHeight());
        return res;
    }
    //endregion
}

