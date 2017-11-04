package com.save_your_own_skin.game_objects;

import base_classes.CharacterObject;
import base_classes.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.save_your_own_skin.game.World;
import com.sun.media.sound.SoftTuning;
import utils.Level;
import utils.Node;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

import static com.save_your_own_skin.game.World.*;

/**
 * Save_your_own_skin
 * Sasha
 * 2017/09/23.
 */
public class Enemy extends CharacterObject
{
    private Stack<Node> path = new Stack<Node>();

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param id
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     * @param damage
     * @param level
     * @param health
     * @param maxHealth
     * @param speed
     */
    public Enemy(int id, Texture texture, int srcWidth, int srcHeight, float damage, int level, int health, int maxHealth, float speed)
    {
        super(id, texture, srcWidth, srcHeight, damage, level, health, maxHealth, speed);
        upgradeToLevel();
    }

    /**
     * Creates a sprite that is a copy in every way of the specified sprite.
     *
     * @param id
     * @param enemy
     */
    public Enemy(int id, int level, Enemy enemy, Node goalNode, Map<Vector2, Node> nodes, float x, float y)
    {
        super(id, enemy, enemy.getDamage(), level, enemy.getHealth(), enemy.getMaxHealth(), enemy.getSpeed());
        super.setPosition(x, y);
        upgradeToLevel();
        this.aStar(goalNode, nodes);
    }

    private void upgradeToLevel()
    {
        for (int i = 0; i < super.getLevel(); i++)
        {
            Level l = new Level(i, this);
            l.incrementLevel();
            l.onLevelChange();
        }
    }

    @Override
    public void onCollision(GameObject collidedObject, float delta)
    {
        if (collidedObject instanceof Player)
        {
            super.destroy();
            super.attack((int) super.getDamage(), (Player) collidedObject);
            return;
        }
        if (collidedObject instanceof CharacterObject) this.move();
    }

    @Override
    public void update(float delta)
    {

    }

    public void move()
    {
        super.setRotation(changeInRotation);
        super.translate(dx, dy);
    }

    public void pointToPlayer(Player player, float delta)
    {
        // Point towards player
        // Rotation
        Vector2 dir = new Vector2(player.getX() - super.getX(), player.getY() - super.getY());
        dir.rotate90(-1);
        changeInRotation = dir.angle();

        // Move
        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        if (angleY == 0)
            angleY += Math.PI;

        float angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
        if (angleX == 0)
            angleX += Math.PI;

        dx = -angleX * super.getSpeed() * delta;
        dy = angleY * super.getSpeed() * delta;
    }

    public void  gotoNextNode(float delta)
    {
        int range = World.TILE_SIZE;
        Node currentNode;
        Vector2 currentNodePos;
        try
        {
            currentNode = path.peek();
            currentNodePos = currentNode.getWorldPos();

            // if within range of current node, pop this node and set current to the next one
            if (super.getX() >= currentNodePos.x - range && super.getY() >= currentNodePos.y - range
                    && super.getX() <= currentNodePos.x + range && super.getY() <= currentNodePos.y + range)
            {
                System.out.println("Moving to next node");
                path.pop();
                currentNode = path.peek();
                currentNodePos = currentNode.getWorldPos();
            }
        }
        catch (EmptyStackException e)
        {
            return;
        }

        // Point towards player
        // Rotation
        Vector2 dir = new Vector2(currentNodePos.x - super.getX(), currentNodePos.y - super.getY());
        dir.rotate90(-1);
        changeInRotation = dir.angle();

        // Move
        float angleY = (float) (Math.cos(Math.toRadians(super.getRotation())));
        if (angleY == 0)
            angleY += Math.PI;

        float angleX = (float) (Math.sin(Math.toRadians(super.getRotation())));
        if (angleX == 0)
            angleX += Math.PI;

        dx = -angleX * super.getSpeed() * delta;
        dy = angleY * super.getSpeed() * delta;

    }

    /**
     * Player = goal
     * this = start
     */
    public void aStar(Node goalNode, Map<Vector2, Node> nodePositions)
    {
        path = new Stack<Node>();
        Queue<Node> openList = new PriorityQueue<Node>(101);
        Queue<Node> closedList = new PriorityQueue<Node>(101);

        Node startNode = nodePositions.get(new Vector2(World.toGridPos(this.getX()), World.toGridPos(this.getY())));

        startNode.parent = null;
        startNode.cost = 0;
        startNode.g = 0;
        startNode.h = estimateDist(goalNode, startNode);
        startNode.f = startNode.h;
        openList.add(startNode);
        while (!openList.isEmpty())
        {
            Node currentNode = openList.poll();
            closedList.add(currentNode);

            // End
            if (currentNode.equals(goalNode))
            {
                // Get final path
                Stack<Node> stack = new Stack<Node>();
                while (currentNode.parent != null)
                {
                    stack.push(currentNode.parent);
                    currentNode = currentNode.parent;
                }
                path = stack;
                return;
            }

            for (Node node : findNeighbours(currentNode, nodePositions))
            {
                if (openList.contains(node) || closedList.contains(node)) continue;

                openList.add(node);
                node.g = currentNode.cost + node.cost;
                node.h = estimateDist(goalNode, node);
                node.f = node.g + node.h;
                node.parent = currentNode;
            }
        }
    }

    /**
     * Delta max
     */
    private int estimateDist(Node goal, Node current)
    {
        int xDist = Math.abs(goal.x - current.x);
        int yDist = Math.abs(goal.y - current.y);

        return Math.max(xDist, yDist);
    }

    private List<Node> findNeighbours(Node n, Map<Vector2, Node> nodePositions)
    {
        List<Node> neighbours = new ArrayList<Node>();
        for (int i = n.x - 1; i < n.x + 1; i++)
        {
            for (int j = n.y - 1; j < n.y + 1; j++)
            {
                Vector2 pos = new Vector2(i, j);
                Node possibleNeighbour = nodePositions.get(pos);
                if (possibleNeighbour != null) neighbours.add(possibleNeighbour);
            }
        }
        return neighbours;
    }

    @Override
    public void destroy()
    {
        super.destroy();
        World.scoreManager.onKill(super.getLevel() / 2, super.getLevel());
    }
}
