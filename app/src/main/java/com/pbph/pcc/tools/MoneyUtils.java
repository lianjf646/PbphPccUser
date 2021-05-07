package com.pbph.pcc.tools;

import com.utils.DoubleUtil;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/9/28.
 */

public class MoneyUtils {

    public static String getMoneyString(double money) {
        return (new DecimalFormat("0.00")).format(money);
    }

    public static String getMoneyString(String money) {
        return (new DecimalFormat("0.00")).format(Double.parseDouble(money));
    }

    public static double scaleMoneyAll(double prise, int count, double tip, int tip_num, double moreTip) {
        if (count == 0) {
            return 0;
        }
        double cost = scaleCost(count, tip, tip_num, moreTip);

        double money = scaleMoney(prise, count);

        return DoubleUtil.sum(cost, money);
    }

    public static double scaleCost(int count, double tip, int tip_num, double moreTip) {

        if (count == 0) {
            return 0;
        }
        double cost = DoubleUtil.mul(1, tip);


        int more_tip_num = count - tip_num;

        if (more_tip_num > 0) {
            double more_cost = DoubleUtil.mul(more_tip_num, moreTip);

            cost = DoubleUtil.sum(cost, more_cost);
        }
        return cost;
    }

    public static double scaleMoney(double prise, int count) {
        if (count == 0) {
            return 0;
        }
        return DoubleUtil.mul(count, prise);

    }


}
