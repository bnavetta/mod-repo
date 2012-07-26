use File::Monitor;
use Data::Dumper;

my $monitor = File::Monitor->new();

$monitor->watch('.', sub {
	my ($name, $event, $change) = @_;
	print "$name: $event\n";
	Dumper($change->old_files);
	print("\n\n ---->\n\n");
	Dumper($change->new_files);
	print("\n\n********************************************\n");
});

$monitor->scan;

while(true)
{
	$monitor->scan;
}
