package com.jml.reggie.dto;

import com.jml.reggie.entity.Setmeal;
import com.jml.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
