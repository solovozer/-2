using CarMaker.Parts;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CarMaker.Factories
{
    internal interface ICarFactory
    {
        string NameModel();
        Engine CreateEngine();
        Gearbox CreateGearbox();
        Wheel CreateWheel();
    }

    internal class HondaFactory : ICarFactory
    {
        public string NameModel() => "Honda Accord";
        public Engine CreateEngine() => new HondaEngine("K20", 150.0, 2.0, FuelType.Petrol, 35.0, true, 4,  false);
        public Gearbox CreateGearbox() => new HondaGearbox("6-Speed", 6, "Manual", 400.0, new double[] { 3.5, 2.1, 1.5, 1.1, 0.9, 0.8 });
        public Wheel CreateWheel() => new HondaWheel(17.0, 215.0, "Alloy", "Bridgestone", 0.85, 12.5, "5-Lug");
    }

    internal class FerrariFactory : ICarFactory
    {
        public string NameModel() => "La Ferrari";
        public Engine CreateEngine() => new FerrariEngine("F154", 600.0, 6.5, FuelType.Electric, 42.0, 8, true);
        public Gearbox CreateGearbox() => new FerrariGearbox("DCT", 7, "Auto", 750.0, new double[] { 4.2, 2.8, 2.1, 1.6, 1.3, 1.1, 0.9 }, 0.05);
        public Wheel CreateWheel() => new FerrariWheel(20.0, 305.0, "Carbon", "Pirelli", 0.95, 15.2, true);
    }

    internal class LadaFactory : ICarFactory
    {
        public string NameModel() => "Lada Niva 4x4";
        public Engine CreateEngine() => new LadaEngine("VAZ-2106", 120.0, 1.6, FuelType.Petrol, 28.0, 4, false);
        public Gearbox CreateGearbox() => new LadaGearbox("Heavy-Duty", 5, "Manual", 150.0, new double[] { 3.8, 2.2, 1.4, 1.0, 0.8 }, true);
        public Wheel CreateWheel() => new LadaWheel(14.0, 175.0, "Reinforced Steel", "Kama", 0.75, 18.5, true);
    }
}
