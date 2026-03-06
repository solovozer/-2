using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CarMaker.Parts
{
    internal class Wheel : VehicleComponent
    {
        private readonly double diameter;
        private readonly double width;
        private readonly string material;
        private readonly string tireBrand;
        private readonly double gripCoefficient;
        private readonly double mass; 

        public double Diameter => diameter;
        public double Width => width;
        public string Material => material;
        public string TireBrand => tireBrand;
        public double GripCoefficient => gripCoefficient;
        public double Mass => mass;


        public Wheel(double diameter, double width, string material, string tireBrand, double gripCoefficient, double mass)
        {
            this.diameter = diameter;
            this.width = width;
            this.material = material;
            this.tireBrand = tireBrand;
            this.gripCoefficient = gripCoefficient;
            this.mass = mass;
        }
    }

    internal class HondaWheel : Wheel
    {
        private readonly string hubType;

        public string HubType => hubType;
        public HondaWheel(double dia, double width, string mat, string brand, double gripCoefficient, double mass, string hubType)
            : base(dia, width, mat, brand, gripCoefficient, mass)
        {
            this.hubType = hubType;
        }
    }

    internal class FerrariWheel : Wheel
    {
        private readonly bool isCenterLock;

        public bool IsCenterLock => isCenterLock;

        public FerrariWheel(double dia, double width, string mat, string brand, double gripCoefficient, double mass, bool isCenterLock)
            : base(dia, width, mat, brand, gripCoefficient, mass)
        {
            this.isCenterLock = isCenterLock;
        }
    }

    internal class LadaWheel : Wheel
    {
        private readonly bool hasSteelFrame;

        public bool HasSteelFrame => hasSteelFrame;

        public LadaWheel(double dia, double width, string mat, string brand, double gripCoefficient, double mass, bool hasSteelFrame)
            : base(dia, width, mat, brand, gripCoefficient, mass)
        {
            this.hasSteelFrame = hasSteelFrame;
        }
    }
}
