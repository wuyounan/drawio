package com.huigou.data.domain.listener;

import java.lang.reflect.Field;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.ReflectionUtils;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.Modifier;

@Configurable
public class CreatorAndModifierListener {

    private static String CREATOR_FIELD_NAME = "creator";

    private static String MODIFIER_FIELD_NAME = "modifier";

    private void setCreatorInfo(AbstractEntity target) {
        Field field = ReflectionUtils.findField(target.getClass(), CREATOR_FIELD_NAME);
        if (field != null) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, target, Creator.newInstance());
        }
    }

    private void setModifierInfo(AbstractEntity target) {
        Field field = ReflectionUtils.findField(target.getClass(), MODIFIER_FIELD_NAME);
        if (field != null) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, target, Modifier.newInstance());
        }
    }

    @PrePersist
    public void beforeCreate(AbstractEntity target) {
        setCreatorInfo(target);
        setModifierInfo(target);
    }

    @PreUpdate
    public void beforeUpdate(AbstractEntity target) {
        setModifierInfo(target);
    }

}
