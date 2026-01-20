using System;
using System.Windows.Forms;
using OrganizationApp.Core.Models;

namespace OrganizationApp
{
    public partial class DirectorForm : Form
    {
        private readonly Organization _org;

        // Constructor mặc định (Designer thường tạo) - giữ lại để Designer không lỗi
        public DirectorForm()
        {
            InitializeComponent();
        }

        // Constructor nhận Organization (để mở từ OrganizationForm)
        public DirectorForm(Organization org) : this()
        {
            _org = org;

            this.Text = "Director Management - OrgID=" + _org.OrgID;

            // lblInfo là Label trong DirectorForm.Designer.cs
            if (lblInfo != null)
            {
                lblInfo.Text =
                    "Director Management\n\n" +
                    "Organization:\n" +
                    "- ID: " + _org.OrgID + "\n" +
                    "- Name: " + _org.OrgName + "\n" +
                    "- Created: " + _org.CreatedDate;
            }
        }
    }
}
