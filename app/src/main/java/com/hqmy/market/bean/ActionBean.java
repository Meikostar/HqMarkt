package com.hqmy.market.bean;

public class ActionBean {
    private int         click;//         0
    private String      created_at;//      2019-05-30 16:44:42
    private boolean     flag1;//      false
    private boolean     flag2;//          false
    private boolean     flag3;//      false
    private boolean     flag4;//      false
    private int         id;//      3
    private String      img;//    images/activity.png
    private String      introduce;//    这里是简介这里是简介这里是简介这里是简介这里是简介
    private String      title;//      校内活动测试
    private String      content;//      校内活动测试
    private ActionExtra extra;//      校内活动测试

    public ActionExtra getExtra() {
        return extra;
    }

    public void setExtra(ActionExtra extra) {
        this.extra = extra;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isFlag1() {
        return flag1;
    }

    public void setFlag1(boolean flag1) {
        this.flag1 = flag1;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }

    public boolean isFlag3() {
        return flag3;
    }

    public void setFlag3(boolean flag3) {
        this.flag3 = flag3;
    }

    public boolean isFlag4() {
        return flag4;
    }

    public void setFlag4(boolean flag4) {
        this.flag4 = flag4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
