using System.Collections.ObjectModel;
using System.Windows;
using System.Collections.Generic;
using System.Windows.Controls;
using System;



namespace StaffManageSystemClient
{

    public partial class ModifyStaffWindow : Window
    {
        private readonly ObservableCollection<Staff> staffList = new ObservableCollection<Staff>();
        private readonly Dictionary<int, Department> dictDepartment = new Dictionary<int, Department>();
        private Staff selectedStaff;

        public List<Staff[]> edits = new List<Staff[]>(); //每个元素是{原，后}
        private ModifyStaffWindow()
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
        public ModifyStaffWindow(Window owner, ObservableCollection<Staff> staffList, ObservableCollection<Department> departmentList) : this()
        {
            Owner = owner;
            Left = owner.Left + owner.Width / 2 - Width / 2;
            Top = owner.Top + owner.Height / 2 - Height / 2;
            foreach (Staff staff in staffList)
            {
                this.staffList.Add(staff.Clone());
            }
            UI_ComboBoxDepartment.ItemsSource = departmentList;
            foreach (Department department in departmentList)
            {
                dictDepartment.Add(department.Id, department);
            }
        }

        private void UI_ButtonEdit_Click(object sender, RoutedEventArgs e)
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
            edits.Add(new Staff[]
            {
                selectedStaff.Clone(),selectedStaff
            });//先复制一份留作修改前的，然后改原件
            selectedStaff.Id = id;
            selectedStaff.Name = name;
            selectedStaff.Gender = gender;
            selectedStaff.Birthday = UI_ComboBoxBirthYear.Text + "-" + UI_ComboBoxBirthMonth.Text + "-" + UI_ComboBoxBirthDay.Text;
            selectedStaff.Telephone = telephone;
            selectedStaff.Department = department;
            selectedStaff.Position = position;
            selectedStaff.Salary = salary;
            selectedStaff.Modified = true;
        }

        private void UI_ListViewStaff_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            selectedStaff = UI_ListViewStaff.SelectedItem as Staff;
            UI_TextBoxID.IsEnabled = selectedStaff != null;
            UI_TextBoxName.IsEnabled = selectedStaff != null;
            UI_ComboBoxGender.IsEnabled = selectedStaff != null;
            UI_TextBoxTelephone.IsEnabled = selectedStaff != null;
            UI_ComboBoxDepartment.IsEnabled = selectedStaff != null;
            UI_TextBoxPosition.IsEnabled = selectedStaff != null;
            UI_ComboBoxBirthYear.IsEnabled = selectedStaff != null;
            UI_ComboBoxBirthMonth.IsEnabled = selectedStaff != null;
            UI_ComboBoxBirthDay.IsEnabled = selectedStaff != null;
            UI_TextBoxSalary.IsEnabled = selectedStaff != null;
            UI_ButtonEdit.IsEnabled = selectedStaff != null;

            if (selectedStaff != null)
            {
                UI_TextBoxID.Text = selectedStaff.id.ToString();
                UI_TextBoxName.Text = selectedStaff.name;
                UI_ComboBoxGender.SelectedIndex = selectedStaff.gender == "男" ? 0 : 1;
                string[] birthdayText = selectedStaff.birthday.Split('-');
                UI_ComboBoxBirthYear.SelectedItem = birthdayText[0];
                UI_ComboBoxBirthMonth.SelectedItem = birthdayText[1];
                UI_ComboBoxBirthDay.SelectedItem = birthdayText[2];
                UI_TextBoxTelephone.Text = selectedStaff.telephone;
                UI_ComboBoxDepartment.SelectedItem = dictDepartment[selectedStaff.department];
                UI_TextBoxPosition.Text = selectedStaff.position;
                UI_TextBoxSalary.Text = selectedStaff.salary.ToString();
            }
        }
        private void UI_ComboBoxBirth_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            ResetDays(); //更改年、月后重新计算日
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

        private void UI_ButtonCommit_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }
    }
}

