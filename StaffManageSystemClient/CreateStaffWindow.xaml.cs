using System;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Controls;


namespace StaffManageSystemClient
{

    public partial class CreateStaffWindow : Window
    {
        public readonly ObservableCollection<Staff> staffList = new ObservableCollection<Staff>();
        private CreateStaffWindow()
        {
            InitializeComponent();
            UI_ListViewStaff.ItemsSource = staffList;
            for (int i = 1900; i <= 2050; i++)
                UI_ComboBoxBirthYear.Items.Add(i.ToString());
            for (int i = 1; i <= 12; i++)
                if (i < 10)
                    UI_ComboBoxBirthMonth.Items.Add("0" + i);
                else
                    UI_ComboBoxBirthMonth.Items.Add(i.ToString());
        }
        public CreateStaffWindow(Window owner, ObservableCollection<Department> departmentList) : this()
        {
            Owner = owner;
            Left = owner.Left + owner.Width / 2 - Width / 2;
            Top = owner.Top + owner.Height / 2 - Height / 2;
            UI_ComboBoxDepartment.ItemsSource = departmentList;
            UI_ComboBoxDepartment.SelectedIndex = 0;
        }

        private void ResetDays()
        {
            UI_ComboBoxBirthDay.Items.Clear();
            string month = (string)UI_ComboBoxBirthMonth.SelectedItem;
            if (month == "02")
            {
                UI_ComboBoxBirthDay.Items.Clear();
                for (int i = 1; i <= 28; i++)
                    if (i < 10)
                        UI_ComboBoxBirthDay.Items.Add("0" + i);
                    else
                        UI_ComboBoxBirthDay.Items.Add(i.ToString());
                int year = Convert.ToInt32((string)UI_ComboBoxBirthYear.SelectedItem);
                if (year % 400 == 0 || year % 100 != 0 && year % 4 == 0)
                    UI_ComboBoxBirthDay.Items.Add("29");
            }
            else
            {
                for (int i = 1; i <= 30; i++)
                    if (i < 10)
                        UI_ComboBoxBirthDay.Items.Add("0" + i);
                    else
                        UI_ComboBoxBirthDay.Items.Add(i.ToString());
                if (month == "01" || month == "03" || month == "05" || month == "07" || month == "08" || month == "10" || month == "12")
                    UI_ComboBoxBirthDay.Items.Add("31");
            }
            UI_ComboBoxBirthDay.SelectedItem = "01";
        }

        private void UI_ButtonAdd_Click(object sender, RoutedEventArgs e)
        {
            if (UI_TextBoxID.Text.Length == 0 || UI_TextBoxName.Text.Length == 0 || UI_TextBoxTelephone.Text.Length == 0 || UI_TextBoxPosition.Text.Length == 0 || UI_TextBoxSalary.Text.Length == 0)
            {
                MessageBox.Show(this, "字段不能为空！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            int id;
            string name = UI_TextBoxName.Text;
            string gender = ((TextBlock)UI_ComboBoxGender.SelectedItem).Text;
            string telephone = UI_TextBoxTelephone.Text;
            int department = ((Department)UI_ComboBoxDepartment.SelectedItem).id;
            string position = UI_TextBoxPosition.Text;
            double salary;

            if (telephone.Length != 11)
            {
                MessageBox.Show(this, "电话号码格式有误！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }
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
            try
            {
                salary = Convert.ToDouble(UI_TextBoxSalary.Text);
                if (salary < 0) throw new FormatException();
            }
            catch (FormatException)
            {
                MessageBox.Show(this, "月薪只能是正浮点数！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }
            staffList.Add(new Staff(id, name, gender, UI_ComboBoxBirthYear.Text + "-" + UI_ComboBoxBirthMonth.Text + "-" + UI_ComboBoxBirthDay.Text, telephone, department, position, salary));
        }

        private void UI_ButtonDelete_Click(object sender, RoutedEventArgs e)
        {
            staffList.Remove((Staff)UI_ListViewStaff.SelectedItem);
        }

        private void UI_ButtonCommit_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }

        private void UI_ListViewStaff_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            UI_ButtonDelete.IsEnabled = UI_ListViewStaff.SelectedItems.Count > 0;
        }

        private void UI_ComboBoxBirth_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            ResetDays(); //更改年、月后重新计算日
        }
    }
}
