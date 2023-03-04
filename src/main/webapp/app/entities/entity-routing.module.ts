import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        data: { pageTitle: 'Employees' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'Companies' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'lead',
        data: { pageTitle: 'Leads' },
        loadChildren: () => import('./lead/lead.module').then(m => m.LeadModule),
      },
      {
        path: 'potential-opportunity',
        data: { pageTitle: 'PotentialOpportunities' },
        loadChildren: () => import('./potential-opportunity/potential-opportunity.module').then(m => m.PotentialOpportunityModule),
      },
      {
        path: 'opportunity',
        data: { pageTitle: 'Opportunities' },
        loadChildren: () => import('./opportunity/opportunity.module').then(m => m.OpportunityModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'Products' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'product-order',
        data: { pageTitle: 'ProductOrders' },
        loadChildren: () => import('./product-order/product-order.module').then(m => m.ProductOrderModule),
      },
      {
        path: 'win-loss',
        data: { pageTitle: 'WinLosses' },
        loadChildren: () => import('./win-loss/win-loss.module').then(m => m.WinLossModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
