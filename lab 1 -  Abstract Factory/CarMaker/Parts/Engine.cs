using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CarMaker.Parts
{
    internal class Engine : VehicleComponent
    {
        private readonly string model;
        private readonly double torque; // Newton-meters
        private readonly double displacement; // Liters
        private readonly FuelType fuel;
        private readonly double thermalEfficiencyPercentage;
        private readonly int cylinders;
        private readonly bool hasTurbo;

        public string Model => model;
        public double Torque => torque;
        public double Displacement => displacement;
        public FuelType Fuel => fuel;
        public double ThermalEfficiencyPercentage => thermalEfficiencyPercentage;
        public int Cylinders => cylinders;
        public bool HasTurbo => hasTurbo;

        public Engine(string model, double torque, double displacement, FuelType fuel, double thermalEfficiencyPercentage, int cylinders, bool hasTurbo)
        {
            this.model = model;
            this.torque = torque;
            this.displacement = displacement;
            this.fuel = fuel;
            this.thermalEfficiencyPercentage = thermalEfficiencyPercentage;
            this.cylinders = cylinders;
            this.hasTurbo = hasTurbo;
        }
    }

    internal class HondaEngine : Engine
    {
        private readonly bool hasVTEC;

        public bool HasVTEC => hasVTEC;

        public HondaEngine(string model, double torque, double displacement, FuelType fuel, double thermalEfficiencyPercentage, bool hasVTEC, int cylinders, bool hasTurbo)
            : base(model, torque, displacement, fuel, thermalEfficiencyPercentage, cylinders, hasTurbo)
        {
            this.hasVTEC = hasVTEC;
        }
    }

    internal class FerrariEngine : Engine
    {
        public FerrariEngine(string model, double torque, double displacement, FuelType fuel, double thermalEfficiencyPercentage, int cylinders, bool hasTurbo)
            : base(model, torque, displacement, fuel, thermalEfficiencyPercentage, cylinders, hasTurbo)
        {
            
        }
    }

    internal class LadaEngine : Engine
    {
        public LadaEngine(string model, double torque, double displacement, FuelType fuel, double thermalEfficiencyPercentage, int cylinders, bool hasTurbo)
            : base(model, torque, displacement, fuel, thermalEfficiencyPercentage, cylinders, hasTurbo)
        {
        }
    }
}
