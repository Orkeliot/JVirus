package Contents;

import Contents.Cells.Cell;
import Contents.Cells.InfectedCell;
import Contents.Virus.Virus;
import Controllers.Game;
import Controllers.Map;
import Enums.Direction;
import Interfaces.Info;
import Utils.IO;

/**
 * Simple class that allows the map to communicates to others classes the positions of its contents.<br>
 * Also implements the menu of its content that will be shown to users after they selected it.<br>
 * (We decided to implement the menu here because we need positional data to ensure that we
 * don't offer to users the opportunity to go outside the map, and because content don't have
 * access to this positional data)
 */
public class LocatedContent {
    public final int x;
    public final int y;
    public final Content content;

    public LocatedContent(int x, int y, Content content) {
        this.x = x;
        this.y = y;
        this.content = content;
    }

    public void menu() {
        IO.print(String.format("Vous avez sélectionné la case %c-%d\n", 'A' + x, y + 1));
        if (content instanceof Info) {
            ((Info) content).info();
        }
        IO.print("Que voulez-vous faire avec cet élément ?\n");
        if (content instanceof InfectedCell) {
            IO.print("1 : Apoptose\n");
        } else {
            if (x != 0) {
                IO.print("4 : Vous déplacer vers la gauche\n");
            }
            if (x != Game.getMap().MAP_SIZE - 1) {
                IO.print("6 : Vous déplacer vers la droite\n");
            }
            if (y != 0) {
                IO.print("8 : Vous déplacer vers le haut\n");
            }
            if (y != Game.getMap().MAP_SIZE - 1) {
                IO.print("2 : Vous déplacer vers le bas\n");
            }
        }
        IO.print("0 : Je ne voulais pas sélectionner cet élément\n");

        int rep = IO.intInput();
        Map map = Game.getMap();
        if (content instanceof InfectedCell) {
            switch (rep) {
                case 1:
                    map.explode(this);
                    break;
                case 0:
                    map.selectLocatedContent(Cell.class).menu();
                    break;
                default:
                    IO.print("Mauvaise entrée\n");
                    menu();
                    break;
            }
        }

        switch (rep) {
            case 8:
                map.move(this, Direction.UP);
                break;
            case 2:
                map.move(this, Direction.DOWN);
                break;
            case 4:
                map.move(this, Direction.LEFT);
                break;
            case 6:
                map.move(this, Direction.RIGHT);
                break;
            case 0:
                if (content instanceof Cell) {
                    map.selectLocatedContent(Cell.class).menu();
                } else {
                    map.selectLocatedContent(Virus.class).menu();
                }
                break;
            case 5:
                break;
            default:
                IO.print("Mauvaise entrée\n");
                menu();
                break;
        }
    }
}
