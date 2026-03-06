using CarMaker.Parts;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CarMaker.Factories
{
    internal class CarManufacturer
    {
        public Car AssembleCar(string carFactory)
        {
                Engine engine;
                Gearbox gearbox;
                Wheel wheel;
                string name = "";

            if (carFactory == "Honda")
            {
                name = "Honda Accord";
                engine = new HondaEngine("K20", 150.0, 2.0, FuelType.Petrol, 35.0, true, 4, false);
                gearbox = new HondaGearbox("6-Speed", 6, "Manual", 400.0, new double[] { 3.5, 2.1, 1.5, 1.1, 0.9, 0.8 });
                wheel = new HondaWheel(17.0, 215.0, "Alloy", "Bridgestone", 0.85, 12.5, "5-Lug");
            }
            else if (carFactory == "Ferrari")
            {
                name = "La Ferrari";
                engine = new FerrariEngine("F154", 600.0, 6.5, FuelType.Electric, 42.0, 8, true);
                gearbox = new FerrariGearbox("DCT", 7, "Auto", 750.0, new double[] { 4.2, 2.8, 2.1, 1.6, 1.3, 1.1, 0.9 }, 0.05);
                wheel = new FerrariWheel(20.0, 305.0, "Carbon", "Pirelli", 0.95, 15.2, true);
            }
            else if (carFactory == "Lada")
            {
                name = "Lada Niva 4x4";
                engine = new LadaEngine("VAZ-2106", 120.0, 1.6, FuelType.Petrol, 28.0, 4, false);
                gearbox = new LadaGearbox("Heavy-Duty", 5, "Manual", 150.0, new double[] { 3.8, 2.2, 1.4, 1.0, 0.8 }, true);
                wheel = new LadaWheel(14.0, 175.0, "Reinforced Steel", "Kama", 0.75, 18.5, true);
            }
            else throw new NullReferenceException();
            return new Car(engine, gearbox, wheel, name);
        }
    }
    internal class Car
    {
        private readonly string model;
        private readonly Engine engine;
        private readonly Gearbox gearbox;
        private readonly Wheel wheel;

        public string Model => model;
        public Engine Engine => engine;
        public Gearbox Gearbox => gearbox;
        public Wheel Wheel => wheel;   

        public Car(Engine engine, Gearbox gearbox, Wheel wheel, string model)
        {
            this.model = model;
            this.engine = engine;
            this.gearbox = gearbox;
            this.wheel = wheel;
        }
    }

    
}
