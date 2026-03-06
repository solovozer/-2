using CarMaker.Factories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CarMaker.Parts;

namespace CarMaker.Service
{
    internal static class ReportingService
    {
        public static string ExportCarReport(Car car)
        {
            string fileName = $"{car.Model.Replace(" ", "_")}_Report.html";
            string reportDate = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");

            string engineDetails = GetEngineDetails(car.Engine);
            string wheelDetails = GetWheelDetails(car.Wheel);
            string gearboxDetails = GetGearboxDetails(car.Gearbox);

            string html = $@"
        <html>
            <head>
                <style>
                    body {{ font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }}
                    h1 {{ color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }}
                    .report-header {{ background-color: #e9ecef; padding: 15px; border-radius: 5px; margin-bottom: 20px; }}
                    .report-date {{ color: #666; font-size: 14px; }}
                    details {{ margin: 15px 0; background-color: white; padding: 15px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }}
                    summary {{ cursor: pointer; font-weight: bold; color: #007bff; padding: 10px; margin: -15px -15px 10px -15px; border-radius: 5px 5px 0 0; background-color: #f0f8ff; }}
                    summary:hover {{ background-color: #e0f0ff; }}
                    .attribute {{ margin: 10px 0; padding: 8px; border-left: 3px solid #007bff; padding-left: 12px; }}
                    .attribute-label {{ font-weight: bold; color: #333; }}
                    .attribute-value {{ color: #666; margin-left: 10px; }}
                </style>
            </head>
            <body>
                <h1>{car.Model} - Official Specification Report</h1>
                
                <div class='report-header'>
                    <p><b>Report Generated:</b> <span class='report-date'>{reportDate}</span></p>
                    <p><b>Vehicle Model:</b> {car.Model}</p>
                </div>

                <details>
                    <summary>🔧 Engine Details</summary>
                    {engineDetails}
                </details>

                <details>
                    <summary>🛞 Wheel Details</summary>
                    {wheelDetails}
                </details>

                <details>
                    <summary>⚙️ Gearbox Details</summary>
                    {gearboxDetails}
                </details>
            </body>
        </html>";

            System.IO.File.WriteAllText(fileName, html);
            return fileName;
        }

        private static string GetEngineDetails(Engine engine)
        {
            string baseDetails = $@"
                <div class='attribute'>
                    <span class='attribute-label'>ID:</span>
                    <span class='attribute-value'>{engine.Id}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Model:</span>
                    <span class='attribute-value'>{engine.Model}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Torque:</span>
                    <span class='attribute-value'>{engine.Torque} Nm</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Displacement:</span>
                    <span class='attribute-value'>{engine.Displacement} L</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Fuel Type:</span>
                    <span class='attribute-value'>{engine.Fuel}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Thermal Efficiency:</span>
                    <span class='attribute-value'>{engine.ThermalEfficiencyPercentage}%</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Cylinders:</span>
                    <span class='attribute-value'>{engine.Cylinders}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Has Turbo:</span>
                    <span class='attribute-value'>{(engine.HasTurbo ? "Yes" : "No")}</span>
                </div>";

            if (engine is HondaEngine hondaEngine)
            {
                baseDetails += $@"
                <div class='attribute'>
                    <span class='attribute-label'>Has VTEC:</span>
                    <span class='attribute-value'>{(hondaEngine.HasVTEC ? "Yes" : "No")}</span>
                </div>";
            }
            return baseDetails;
        }

        private static string GetWheelDetails(Wheel wheel)
        {
            string baseDetails = $@"
                <div class='attribute'>
                    <span class='attribute-label'>ID:</span>
                    <span class='attribute-value'>{wheel.Id}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Diameter:</span>
                    <span class='attribute-value'>{wheel.Diameter} inches</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Width:</span>
                    <span class='attribute-value'>{wheel.Width} mm</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Material:</span>
                    <span class='attribute-value'>{wheel.Material}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Tire Brand:</span>
                    <span class='attribute-value'>{wheel.TireBrand}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Grip Coefficient:</span>
                    <span class='attribute-value'>{wheel.GripCoefficient}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Mass:</span>
                    <span class='attribute-value'>{wheel.Mass} kg</span>
                </div>";

            if (wheel is HondaWheel hondaWheel)
            {
                baseDetails += $@"
                <div class='attribute'>
                    <span class='attribute-label'>Hub Type:</span>
                    <span class='attribute-value'>{hondaWheel.HubType}</span>
                </div>";
            }
            else if (wheel is FerrariWheel ferrariWheel)
            {
                baseDetails += $@"
                <div class='attribute'>
                    <span class='attribute-label'>Is Center Lock:</span>
                    <span class='attribute-value'>{(ferrariWheel.IsCenterLock ? "Yes" : "No")}</span>
                </div>";
            }
            else if (wheel is LadaWheel ladaWheel)
            {
                baseDetails += $@"
                <div class='attribute'>
                    <span class='attribute-label'>Has Steel Frame:</span>
                    <span class='attribute-value'>{(ladaWheel.HasSteelFrame ? "Yes" : "No")}</span>
                </div>";
            }

            return baseDetails;
        }

        private static string GetGearboxDetails(Gearbox gearbox)
        {
            string gearRatiosString = string.Join(", ", gearbox.GearRatios.Select(r => r.ToString("F1")));
            
            string baseDetails = $@"
                <div class='attribute'>
                    <span class='attribute-label'>ID:</span>
                    <span class='attribute-value'>{gearbox.Id}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Model:</span>
                    <span class='attribute-value'>{gearbox.Model}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Gear Count:</span>
                    <span class='attribute-value'>{gearbox.GearCount}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Type:</span>
                    <span class='attribute-value'>{gearbox.Type}</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Max Torque:</span>
                    <span class='attribute-value'>{gearbox.MaxTorque} Nm</span>
                </div>
                <div class='attribute'>
                    <span class='attribute-label'>Gear Ratios:</span>
                    <span class='attribute-value'>{gearRatiosString}</span>
                </div>";

            if (gearbox is FerrariGearbox ferrariGearbox)
            {
                baseDetails += $@"
                <div class='attribute'>
                    <span class='attribute-label'>Shift Speed:</span>
                    <span class='attribute-value'>{ferrariGearbox.ShiftSpeedMs} ms</span>
                </div>";
            }
            else if (gearbox is LadaGearbox ladaGearbox)
            {
                baseDetails += $@"
                <div class='attribute'>
                    <span class='attribute-label'>Has Low Range:</span>
                    <span class='attribute-value'>{(ladaGearbox.HasLowRange ? "Yes" : "No")}</span>
                </div>";
            }

            return baseDetails;
        }
    }
}
