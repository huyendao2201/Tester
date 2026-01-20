using System.Collections.Generic;
using OrganizationApp.Core.Data;
using OrganizationApp.Core.Models;
using OrganizationApp.Core.Validation;

namespace OrganizationApp.Core.Services
{
    public class SaveOrganizationResult
    {
        public bool Success { get; set; }
        public int? NewOrgId { get; set; }
        public string Message { get; set; }
        public List<ValidationError> Errors { get; set; }

        public SaveOrganizationResult()
        {
            Errors = new List<ValidationError>();
        }
    }

    public class OrganizationService
    {
        private readonly OrganizationRepository _repo;

        public OrganizationService(OrganizationRepository repo)
        {
            _repo = repo;
        }

        public SaveOrganizationResult Save(Organization input)
        {
            input.OrgName = (input.OrgName ?? "").Trim();
            input.Address = (input.Address ?? "").Trim();
            input.Phone = (input.Phone ?? "").Trim();
            input.Email = (input.Email ?? "").Trim();

            var errors = OrganizationValidator.ValidateInput(input);
            if (errors.Count > 0)
            {
                return new SaveOrganizationResult
                {
                    Success = false,
                    Message = "Validation failed",
                    Errors = errors
                };
            }

            if (_repo.ExistsByName(input.OrgName))
            {
                return new SaveOrganizationResult
                {
                    Success = false,
                    Message = "Organization Name already exists"
                };
            }

            int newId = _repo.Insert(input);
            return new SaveOrganizationResult
            {
                Success = true,
                NewOrgId = newId,
                Message = "Save successfully"
            };
        }
    }
}
