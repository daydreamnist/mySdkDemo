package com.broadlink.mysdkdemo.model;

import java.io.Serializable;
import java.util.List;

public class ProductInfo implements Serializable{
    private static final long serialVersionUID = 7784629427007340255L;

    private String pid;
    private int platformtype;
    private int status;
    private String name;
    private String icon;
    private String shortcuticon;
    private List<String> ads;
    private List<Introduction> introduction;
    private String beforecfgpurl;
    private String configpic;
    private String cfgfailedurl;
    private String depid;
    private String description;
    private String brandid;
    private String brandname;
    private int protocol;
    private String model;
    private String type;
    private String productversion;
    private String productkitversion;
    private String scripturl;
    private String uiurl;
    private String manufurl;
    private String profileurl;
    private String productstatus;
    private String scripttype;
    private String alias;
    private String resetpic;
    private String resettext;
    private String configtext;
    private int commonflag;
    private String category;
    private String mappid;
    private String profile;

    public static class Introduction implements Serializable {

        private static final long serialVersionUID = 399370100673616469L;

        private String name;

        private String icon;

        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getPlatformtype() {
        return platformtype;
    }

    public void setPlatformtype(int platformtype) {
        this.platformtype = platformtype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getShortcuticon() {
        return shortcuticon;
    }

    public void setShortcuticon(String shortcuticon) {
        this.shortcuticon = shortcuticon;
    }

    public List<String> getAds() {
        return ads;
    }

    public void setAds(List<String> ads) {
        this.ads = ads;
    }

    public List<Introduction> getIntroduction() {
        return introduction;
    }

    public void setIntroduction(List<Introduction> introduction) {
        this.introduction = introduction;
    }

    public String getBeforecfgpurl() {
        return beforecfgpurl;
    }

    public void setBeforecfgpurl(String beforecfgpurl) {
        this.beforecfgpurl = beforecfgpurl;
    }

    public String getConfigpic() {
        return configpic;
    }

    public void setConfigpic(String configpic) {
        this.configpic = configpic;
    }

    public String getCfgfailedurl() {
        return cfgfailedurl;
    }

    public void setCfgfailedurl(String cfgfailedurl) {
        this.cfgfailedurl = cfgfailedurl;
    }

    public String getDepid() {
        return depid;
    }

    public void setDepid(String depid) {
        this.depid = depid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductversion() {
        return productversion;
    }

    public void setProductversion(String productversion) {
        this.productversion = productversion;
    }

    public String getProductkitversion() {
        return productkitversion;
    }

    public void setProductkitversion(String productkitversion) {
        this.productkitversion = productkitversion;
    }

    public String getScripturl() {
        return scripturl;
    }

    public void setScripturl(String scripturl) {
        this.scripturl = scripturl;
    }

    public String getUiurl() {
        return uiurl;
    }

    public void setUiurl(String uiurl) {
        this.uiurl = uiurl;
    }

    public String getManufurl() {
        return manufurl;
    }

    public void setManufurl(String manufurl) {
        this.manufurl = manufurl;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(String productstatus) {
        this.productstatus = productstatus;
    }

    public String getScripttype() {
        return scripttype;
    }

    public void setScripttype(String scripttype) {
        this.scripttype = scripttype;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getResetpic() {
        return resetpic;
    }

    public void setResetpic(String resetpic) {
        this.resetpic = resetpic;
    }

    public String getResettext() {
        return resettext;
    }

    public void setResettext(String resettext) {
        this.resettext = resettext;
    }

    public String getConfigtext() {
        return configtext;
    }

    public void setConfigtext(String configtext) {
        this.configtext = configtext;
    }

    public int getCommonflag() {
        return commonflag;
    }

    public void setCommonflag(int commonflag) {
        this.commonflag = commonflag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMappid() {
        return mappid;
    }

    public void setMappid(String mappid) {
        this.mappid = mappid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
