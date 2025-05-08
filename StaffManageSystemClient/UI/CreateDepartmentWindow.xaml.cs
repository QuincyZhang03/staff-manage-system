using System;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Controls;


namespace StaffManageSystemClient
{

    public partial class CreateDepartmentWindow : Window
    {
        public readonly ObservableCollection<Department> departmentList = new ObservableCollection<Department>();

        public CreateDepartmentWindow()
        {
            InitializeComponent();
            UI_ListViewDepartment.ItemsSource = departmentList;
        }
        public CreateDepartmentWindow(Window owner) : this()
        {
            Owner = owner;
            Left = owner.Left + owner.Width / 2 - Width / 2;
            Top = owner.Top + owner.Height / 2 - Height / 2;
        }

        private void UI_ButtonAdd_Click(object sender, RoutedEventArgs e)
        {
            if (UI_TextBoxID.Text.Length == 0 || UI_TextBoxName.Text.Length == 0)
            {
                MessageBox.Show(this, "字段不能为空！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            int id;
            string name = UI_TextBoxName.Text;
            try
            {
                id = Convert.ToInt32(UI_TextBoxID.Text);
                if (id < 0) throw new FormatException();
            }
            catch (FormatException)
            {
                MessageBox.Show(this, "编号只能是正整数！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }
            departmentList.Add(new Department(id, name));
        }
        private void UI_ButtonDelete_Click(object sender, RoutedEventArgs e)
        {
            departmentList.Remove((Department)UI_ListViewDepartment.SelectedItem);
        }
        private void UI_ButtonCommit_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }

        private void UI_ListViewDepartment_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            UI_ButtonDelete.IsEnabled = UI_ListViewDepartment.SelectedItems.Count > 0;
        }
    }
}
