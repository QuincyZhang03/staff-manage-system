using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Controls;


namespace StaffManageSystemClient
{
    public partial class ModifyDepartmentWindow : Window
    {
        private readonly ObservableCollection<Department> departmentList = new ObservableCollection<Department>();
        private Department selectedDepartment;
        public List<Department[]> edits = new List<Department[]>(); //每个元素是{原，后}

        public ModifyDepartmentWindow()
        {
            InitializeComponent();
            UI_ListViewDepartment.ItemsSource = departmentList;
        }
        public ModifyDepartmentWindow(Window owner, ObservableCollection<Department> departmentList) : this()
        {
            Owner = owner;
            Left = owner.Left + owner.Width / 2 - Width / 2;
            Top = owner.Top + owner.Height / 2 - Height / 2;
            foreach (Department department in departmentList)
            {
                this.departmentList.Add(department.Clone());
            }
        }

        private void UI_ButtonEdit_Click(object sender, RoutedEventArgs e)
        {
            if (UI_TextBoxID.Text.Length == 0 || UI_TextBoxName.Text.Length == 0 )
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
           
            edits.Add(new Department[]
            {
                selectedDepartment.Clone(),selectedDepartment
            });//先复制一份留作修改前的，然后改原件
            selectedDepartment.Id = id;
            selectedDepartment.Name = name;
            selectedDepartment.Modified = true;
        }

        private void UI_ListViewDepartment_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            selectedDepartment = UI_ListViewDepartment.SelectedItem as Department;
            UI_TextBoxID.IsEnabled = selectedDepartment != null;
            UI_TextBoxName.IsEnabled = selectedDepartment != null;
            UI_ButtonEdit.IsEnabled = selectedDepartment != null;

            if (selectedDepartment != null)
            {
                UI_TextBoxID.Text = selectedDepartment.id.ToString();
                UI_TextBoxName.Text = selectedDepartment.name;
            }
        }
        private void UI_ButtonCommit_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }
    }
}
