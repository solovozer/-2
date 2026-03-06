using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CarMaker.Parts
{

    internal abstract class VehicleComponent
    {
        protected readonly string id;

        public string Id => id;

        protected VehicleComponent()
        {
            // Generate a unique ID using Guid
            id = Guid.NewGuid().ToString();
        }
    }
}
