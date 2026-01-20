using System;
using System.Text;
using System.Windows.Forms;
using OrganizationApp.Core.Data;
using OrganizationApp.Core.Models;
using OrganizationApp.Core.Services;

namespace OrganizationApp
{
    public partial class OrganizationForm : Form
    {
        private readonly string _connectionString;
        private int? _savedOrgId = null;

        public OrganizationForm(string connectionString)
        {
            InitializeComponent();
            _connectionString = connectionString;

            btnDirector.Enabled = false; // theo đề
        }

        private void btnBack_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            var repo = new OrganizationRepository(_connectionString);
            var service = new OrganizationService(repo);

            var org = new Organization
            {
                OrgName = txtOrgName.Text,
                Address = txtAddress.Text,
                Phone = txtPhone.Text,
                Email = txtEmail.Text
            };

            var result = service.Save(org);

            if (!result.Success)
            {
                // duplicate đúng message đề
                if (result.Message == "Organization Name already exists")
                {
                    MessageBox.Show(result.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    btnDirector.Enabled = false;
                    _savedOrgId = null;
                    return;
                }

                // show lỗi theo từng field
                var sb = new StringBuilder();
                foreach (var er in result.Errors)
                    sb.AppendLine(er.Field + ": " + er.Message);

                MessageBox.Show(sb.ToString().Trim(), "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                btnDirector.Enabled = false;
                _savedOrgId = null;
                return;
            }

            _savedOrgId = result.NewOrgId;
            btnDirector.Enabled = true;
            MessageBox.Show(result.Message ?? "Save successfully", "Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void btnDirector_Click(object sender, EventArgs e)
        {
            if (_savedOrgId == null) return;

            var repo = new OrganizationRepository(_connectionString);
            var org = repo.GetById(_savedOrgId.Value);

            if (org == null)
            {
                MessageBox.Show("Organization not found.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            using (var f = new DirectorForm(org))
            {
                f.ShowDialog(this);
            }
        }
    }
}
