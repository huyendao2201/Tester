using System;
using System.Data.SQLite;
using OrganizationApp.Core.Models;

namespace OrganizationApp.Core.Data
{
    public class OrganizationRepository
    {
        private readonly string _connectionString;

        public OrganizationRepository(string connectionString)
        {
            _connectionString = connectionString;
            DbInitializer.EnsureCreated(_connectionString);
        }

        public bool ExistsByName(string orgName)
        {
            orgName = (orgName ?? "").Trim();

            using (var conn = new SQLiteConnection(_connectionString))
            {
                conn.Open();
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = "SELECT 1 FROM ORGANIZATION WHERE OrgName = @name COLLATE NOCASE LIMIT 1;";
                    cmd.Parameters.AddWithValue("@name", orgName);
                    return cmd.ExecuteScalar() != null;
                }
            }
        }

        public int Insert(Organization org)
        {
            using (var conn = new SQLiteConnection(_connectionString))
            {
                conn.Open();
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = @"
INSERT INTO ORGANIZATION (OrgName, Address, Phone, Email)
VALUES (@name, @address, @phone, @email);
SELECT last_insert_rowid();";

                    cmd.Parameters.AddWithValue("@name", (org.OrgName ?? "").Trim());
                    cmd.Parameters.AddWithValue("@address", (object)(org.Address ?? "").Trim() == "" ? DBNull.Value : (object)org.Address.Trim());
                    cmd.Parameters.AddWithValue("@phone", (object)(org.Phone ?? "").Trim() == "" ? DBNull.Value : (object)org.Phone.Trim());
                    cmd.Parameters.AddWithValue("@email", (object)(org.Email ?? "").Trim() == "" ? DBNull.Value : (object)org.Email.Trim());

                    return Convert.ToInt32(cmd.ExecuteScalar());
                }
            }
        }

        public Organization GetById(int id)
        {
            using (var conn = new SQLiteConnection(_connectionString))
            {
                conn.Open();
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = @"
SELECT OrgID, OrgName, Address, Phone, Email, CreatedDate
FROM ORGANIZATION WHERE OrgID = @id;";
                    cmd.Parameters.AddWithValue("@id", id);

                    using (var r = cmd.ExecuteReader())
                    {
                        if (!r.Read()) return null;

                        var org = new Organization();
                        org.OrgID = r.GetInt32(0);
                        org.OrgName = r.GetString(1);
                        org.Address = r.IsDBNull(2) ? null : r.GetString(2);
                        org.Phone = r.IsDBNull(3) ? null : r.GetString(3);
                        org.Email = r.IsDBNull(4) ? null : r.GetString(4);

                        // SQLite datetime('now') trả string
                        DateTime dt;
                        if (DateTime.TryParse(r.GetString(5), out dt)) org.CreatedDate = dt;
                        else org.CreatedDate = DateTime.Now;

                        return org;
                    }
                }
            }
        }
    }
}
