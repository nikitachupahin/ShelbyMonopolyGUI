package GameLogic;

import java.util.Comparator;


public class PlayerComparator implements Comparator<Player> {

    @Override
    public int compare(Player arg0, Player arg1) {
        if (!arg0.isOnline()) return 1;
        if (!arg1.isOnline()) return -1;
        int flag = arg1.getMoneyAmount() - arg0.getMoneyAmount();
        if (flag == 0) {
            return arg0.getPlayerNum() - arg1.getPlayerNum();
        } else {
            return flag;
        }
    }

}
