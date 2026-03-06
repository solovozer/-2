using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using System.Web;



namespace Undertale
{
    public static class LVHP
    {
        public static readonly int[] MaxHPTable =
        {
        20, 24, 28, 32, 36, 40, 44, 48, 52, 56,
        60, 64, 68, 72, 76, 80, 84, 88, 92, 99
        };

        public static int GetHP(int level)
        {
            if (level < MaxHPTable.Length) { return MaxHPTable[level - 1]; } else throw new ArgumentOutOfRangeException();
        }
    }
    internal class Player
    {
        private int level;
        private int currHP;
        private double speed;

        public int Level => level;
        public int MaxHP => LVHP.GetHP(Level);
        public int CurrentHP => currHP;
        public double Speed => speed;
        
        public Player(int level, double speed = 5)
        {
            this.level = level;
            currHP = LVHP.GetHP(Level);
            this.speed = speed;
        }

    }
}
