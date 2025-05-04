using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows;

namespace StaffManageSystemClient
{
    public partial class LevelSelect : Window
    {
        private readonly int originalLevel;
        private readonly ObservableCollection<LevelItem> items = new ObservableCollection<LevelItem>();
        private readonly Dictionary<int, LevelItem> levelMap = new Dictionary<int, LevelItem>();

        public bool changed = false;
        public int selectedLevel;
        public string key;

        public LevelSelect()
        {
            InitializeComponent();
            foreach (int i in User.AVAILABLE_LEVELS)
            {
                LevelItem item = new LevelItem(i);
                levelMap.Add(i, item); //方便查找
                items.Add(item);
            }
            UI_ComboBoxLevel.ItemsSource = items;
        }
        public LevelSelect(Window owner, string username, int level) : this()
        {
            Owner = owner;
            Left = owner.Left + owner.Width / 2 - Width / 2;
            Top = owner.Top + owner.Height / 2 - Height / 2;
            originalLevel = level;
            levelMap[originalLevel].Original = true;
            UI_ComboBoxLevel.SelectedItem = levelMap[originalLevel];
            UI_TextUsername.Text = username;
        }

        private void UI_ButtonCommit_Click(object sender, RoutedEventArgs e)
        {
            if (UI_PasswordKey.Password.Length == 0)
            {
                MessageBox.Show(this, "敏感权限秘钥不能为空，请重新输入！", "输入有误", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else
            {
                key = UI_PasswordKey.Password;
                Close();
            }
        }

        private void UI_ComboBoxLevel_SelectionChanged(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
            selectedLevel = ((LevelItem)UI_ComboBoxLevel.SelectedItem).level;
            changed = selectedLevel != originalLevel;
            UI_ButtonCommit.IsEnabled = selectedLevel != originalLevel;
        }
    }
    public class LevelItem
    {
        public int level;
        public bool Original { get; set; }
        public string LevelName
        {
            get => User.GetLevelDisplay(level);
        }

        public LevelItem(int level)
        {
            this.level = level;
        }
    }
}
