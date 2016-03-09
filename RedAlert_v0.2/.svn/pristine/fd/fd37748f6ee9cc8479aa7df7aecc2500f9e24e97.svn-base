package com.youxigu.dynasty2.entity.domain;

import com.youxigu.dynasty2.util.BaseException;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class EntityConsumeEx implements Serializable {

    private static final long serialVersionUID = -1703333063883608011L;

    private int id;

    private int entId;// 元实体id

    private int level;// 元实体等级

    private String needEntId;// 需要的实体id

    private String needEntNum;// 需要的实体数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNeedEntId() {
        return needEntId;
    }

    public void setNeedEntId(String needEntId) {
        this.needEntId = needEntId;
    }

    public String getNeedEntNum() {
        return needEntNum;
    }

    public void setNeedEntNum(String needEntNum) {
        this.needEntNum = needEntNum;
    }

    public EntityConsumeEx clone() {
        EntityConsumeEx consume = new EntityConsumeEx();
        consume.setId(this.id);
        consume.setEntId(this.entId);
        consume.setLevel(this.level);
        consume.setNeedEntId(this.needEntId);
        consume.setNeedEntNum(this.needEntNum);
        return consume;
    }

    public List<EntityConsume> toEntityConsumes() {
        List<EntityConsume> result = new LinkedList<EntityConsume>();
        if (needEntId == null || needEntId.length() == 0
                || needEntNum == null || needEntNum.length() == 0) {
            throw new BaseException("错误的EntityConsume配置：" + entId);
        }
        String[] entIds = needEntId.split(",");
        String[] entNums = needEntNum.split(",");

        if (entIds.length != entNums.length) {
            throw new BaseException("错误的EntityConsume配置：(" + entId + ")id与num数量不匹配");
        }

        for (int i = 0; i < entIds.length; i++) {
            try {
                int needEntId = Integer.parseInt(entIds[i]);
                int needEntNum = Integer.parseInt(entNums[i]);

                EntityConsume consume = new EntityConsume();
                consume.setEntId(this.entId);
                consume.setNeedEntId(needEntId);
                consume.setNeedEntNum(needEntNum);
                consume.setLevel(this.level);
                //此处可能造成多个consume的id相同，但因为不写数据库，此id也无其它用途，没有副作用
                consume.setId(this.id);

                result.add(consume);
            } catch (NumberFormatException e) {
                throw new BaseException("错误的EntityConsume配置：(" + entId +
                        ")needEntId或needEntNum格式错误");
            }
        }

        return result;
    }
}
