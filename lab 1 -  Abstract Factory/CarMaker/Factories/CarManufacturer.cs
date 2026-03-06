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
        public Car AssembleCar(ICarFactory carFactory)
        {
            string name = carFactory.NameModel();
            Engine engine = carFactory.CreateEngine();
            Gearbox gearbox = carFactory.CreateGearbox();
            Wheel wheel = carFactory.CreateWheel();
            
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
