using System;
using System.IO;
using System.Windows.Forms;

namespace OrganizationApp
{
    static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            string dbPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "org.db");
            string connectionString = "Data Source=" + dbPath + ";Version=3;";

            Application.Run(new OrganizationForm(connectionString));
        }
    }
}
