using System.Collections.Generic;
using System.Text.RegularExpressions;
using OrganizationApp.Core.Models;

namespace OrganizationApp.Core.Validation
{
    public static class OrganizationValidator
    {
        private static readonly Regex EmailRegex = new Regex(
            @"^[^@\s]+@[^@\s]+\.[^@\s]+$",
            RegexOptions.Compiled | RegexOptions.CultureInvariant);

        public static List<ValidationError> ValidateInput(Organization org)
        {
            var errors = new List<ValidationError>();

            var name = (org.OrgName ?? "").Trim();
            if (string.IsNullOrWhiteSpace(name))
            {
                errors.Add(new ValidationError("OrgName", "Organization Name is required"));
            }
            else if (name.Length < 3 || name.Length > 255)
            {
                errors.Add(new ValidationError("OrgName", "Organization Name length must be 3–255 characters"));
            }

            var email = (org.Email ?? "").Trim();
            if (email.Length > 0)
            {
                if (email.Length > 100)
                    errors.Add(new ValidationError("Email", "Email max length is 100"));
                else if (!EmailRegex.IsMatch(email))
                    errors.Add(new ValidationError("Email", "Email is not in a valid format"));
            }

            var phone = (org.Phone ?? "").Trim();
            if (phone.Length > 0)
            {
                for (int i = 0; i < phone.Length; i++)
                {
                    if (!char.IsDigit(phone[i]))
                    {
                        errors.Add(new ValidationError("Phone", "Phone must contain digits only"));
                        break;
                    }
                }

                if (phone.Length < 9 || phone.Length > 12)
                    errors.Add(new ValidationError("Phone", "Phone length must be 9–12 digits"));
            }

            return errors;
        }
    }
}
