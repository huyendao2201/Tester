using Microsoft.VisualStudio.TestTools.UnitTesting;
using OrganizationApp.Core.Data;
using OrganizationApp.Core.Models;
using OrganizationApp.Core.Services;
using System;
using System.IO;

namespace OrganizationApp.Tests
{
    [TestClass]
    public class OrganizationServiceTests
    {
        private string CreateTempDb()
        {
            string dbFile = Path.Combine(Path.GetTempPath(), "org_test_" + Guid.NewGuid().ToString("N") + ".db");
            return "Data Source=" + dbFile + ";Version=3;";
        }

        [TestMethod]
        public void Save_DuplicateName_CaseInsensitive_ShouldFail()
        {
            string cs = CreateTempDb();
            var repo = new OrganizationRepository(cs);
            var service = new OrganizationService(repo);

            var r1 = service.Save(new Organization { OrgName = "Acme" });
            Assert.IsTrue(r1.Success);

            var r2 = service.Save(new Organization { OrgName = "acme" });
            Assert.IsFalse(r2.Success);
            Assert.AreEqual("Organization Name already exists", r2.Message);
        }
    }
}
