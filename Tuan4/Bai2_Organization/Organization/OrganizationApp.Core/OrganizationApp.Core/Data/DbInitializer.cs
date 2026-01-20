using System.Data.SQLite;

namespace OrganizationApp.Core.Data
{
    public static class DbInitializer
    {
        public static void EnsureCreated(string connectionString)
        {
            using (var conn = new SQLiteConnection(connectionString))
            {
                conn.Open();

                // Table
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = @"
CREATE TABLE IF NOT EXISTS ORGANIZATION (
    OrgID       INTEGER PRIMARY KEY AUTOINCREMENT,
    OrgName     TEXT NOT NULL,
    Address     TEXT NULL,
    Phone       TEXT NULL,
    Email       TEXT NULL,
    CreatedDate TEXT NOT NULL DEFAULT (datetime('now'))
);";
                    cmd.ExecuteNonQuery();
                }

                // Unique case-insensitive: dùng index với COLLATE NOCASE
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = @"
CREATE UNIQUE INDEX IF NOT EXISTS UX_ORGANIZATION_OrgName_NoCase
ON ORGANIZATION(OrgName COLLATE NOCASE);";
                    cmd.ExecuteNonQuery();
                }
            }
        }
    }
}
