package utils;

import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game.World;

public class Node implements Comparable
{
    public int h;
    public int g;
    public int f;
    public int cost;

    public int x, y;

    public Node parent;


    public Node(int x, int y, int cost)
    {
        this.cost = cost;

        this.x = x;
        this.y = y;
    }

    public Vector2 getWorldPos()
    {
        return new Vector2(World.toAbsolutePos(x), World.toAbsolutePos(y));
    }

    @Override
    public int compareTo(Object o)
    {
        return this.f >= ((Node) o).f ? 1 : -1;
    }
}
