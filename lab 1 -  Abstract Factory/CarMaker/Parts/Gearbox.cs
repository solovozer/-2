using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace CarMaker.Parts
{
    internal class Gearbox : VehicleComponent
    {
        private readonly string model;
        private readonly int gearCount;
        private readonly string type;
        private readonly double maxTorque;
        private readonly double[] gearRatios; 

        public string Model => model;
        public int GearCount => gearCount;
        public string Type => type;
        public double MaxTorque => maxTorque;
        public double[] GearRatios => gearRatios;

        public Gearbox(string model, int gearCount, string type, double maxTorque, double[] gearRatios)
        {
            this.model = model;
            this.gearCount = gearCount; 
            this.type = type;
            this.maxTorque = maxTorque;
            this.gearRatios = gearRatios;
        }
    }

    internal class HondaGearbox : Gearbox
    {

        public HondaGearbox(string model, int gearCount, string type, double maxTorque, double[] gearRatios)
            : base(model, gearCount, type, maxTorque, gearRatios) {}
    }

    internal class FerrariGearbox : Gearbox
    {
        private readonly double shiftSpeedMs;
        public double ShiftSpeedMs => shiftSpeedMs;
        public FerrariGearbox(string model, int gearCount, string type, double maxTorque, double[] gearRatios, double shiftSpeedMs)
            : base(model, gearCount, type, maxTorque, gearRatios)
        {
            this.shiftSpeedMs = shiftSpeedMs;
        }
    }

    internal class LadaGearbox : Gearbox
    {
        private readonly bool hasLowRange; // For 4x4 Niva 

        public bool HasLowRange => hasLowRange;

        public LadaGearbox(string model, int gearCount, string type, double maxTorque, double[] gearRatios, bool hasLowRange)
            : base(model, gearCount, type, maxTorque, gearRatios)
        {
            this.hasLowRange = hasLowRange;
        }
    }
}
