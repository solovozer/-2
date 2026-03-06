using CarMaker.Factories;
using CarMaker.Service;
using System.Collections.ObjectModel;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;


namespace CarMaker
{
    using System.IO;

    public partial class MainWindow : Window
    {
        // Define a wrapper for the history table
        internal class CarRecord
        {
            public Car Car { get; set; }
            public string ReportPath { get; set; }
            public DateTime CreationTime { get; set; }
        }

        private ObservableCollection<CarRecord> _history = new ObservableCollection<CarRecord>();

        public MainWindow()
        {
            InitializeComponent();
            HistoryGrid.ItemsSource = _history;
        }

        private void MakeCar(object sender, RoutedEventArgs e)
        {
            string factory = BrandSelector.Text switch
            {
                "Honda" => "Honda",
                "Ferrari" => "Ferrari",
                _ => "Lada"
            };

            var assembler = new CarManufacturer();
            Car car = assembler.AssembleCar(factory);
            string reportPath = ReportingService.ExportCarReport(car);

            //Display frontend and photo
            ModelDisplayName.Text = car.Model;

            string baseDirectory = AppDomain.CurrentDomain.BaseDirectory;
            string imagePath = Path.Combine(baseDirectory, "..", "..", "..", "Images", $"{car.Model}.jpg");
            imagePath = Path.GetFullPath(imagePath);
            if (File.Exists(imagePath))
            {
                CarPhoto.Source = new BitmapImage(new Uri(imagePath));
            }
           

            //update history table
            _history.Insert(0, new CarRecord
            {
                Car = car,
                ReportPath = reportPath,
                CreationTime = DateTime.Now
            });
            if (_history.Count > 5) _history.RemoveAt(5);
        }

        //Open html report
        private void OpenReport(object sender, RoutedEventArgs e)
        {
            var hyperlink = (Hyperlink)sender;
            var record = (CarRecord)hyperlink.DataContext;

            if (record != null && System.IO.File.Exists(record.ReportPath))
            {
                System.Diagnostics.Process.Start(new System.Diagnostics.ProcessStartInfo(record.ReportPath)
                {
                    UseShellExecute = true
                });
            }
        }
    }
}